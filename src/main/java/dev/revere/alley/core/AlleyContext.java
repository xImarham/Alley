package dev.revere.alley.core;

import dev.revere.alley.Alley;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.core.lifecycle.IService;
import dev.revere.alley.tool.logger.Logger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
@Getter
public final class AlleyContext {
    private static final String SERVICE_IMPL_PACKAGE = "dev.revere.alley";

    private final Alley plugin;
    private final Map<Class<? extends IService>, IService> serviceInstances = new ConcurrentHashMap<>();

    private final Map<Class<? extends IService>, Class<? extends IService>> serviceRegistry = new HashMap<>();
    private final Set<Class<? extends IService>> servicesBeingConstructed = new HashSet<>();

    private List<IService> sortedServices = Collections.emptyList();

    public AlleyContext(Alley plugin) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin instance cannot be null");
    }

    /**
     * Discovers, instantiates, and initializes all services.
     */
    public void initialize() throws Exception {
        Logger.logPhaseStart("Service Initialization");

        // 1. Discover all service implementation classes ONCE.
        List<Class<? extends IService>> implementationClasses = discoverServices();
        if (implementationClasses.isEmpty()) {
            throw new IllegalStateException("No services found to load.");
        }

        // 2. Build the Interface -> Implementation registry map.
        for (Class<? extends IService> implClass : implementationClasses) {
            serviceRegistry.put(getProvidedInterface(implClass), implClass);
        }

        // 3. Sort services by priority for ordered initialization.
        List<Class<? extends IService>> sortedImplClasses = sortServicesByPriority(implementationClasses);
        Logger.info("Service Initialization Order: " +
                sortedImplClasses.stream().map(Class::getSimpleName).collect(Collectors.joining(" -> ")));

        // 4. Instantiate all services.
        for (Class<? extends IService> implClass : sortedImplClasses) {
            instantiateService(implClass);
        }

        this.sortedServices = sortedImplClasses.stream()
                .map(impl -> serviceInstances.get(getProvidedInterface(impl)))
                .collect(Collectors.toList());

        // 5. Run lifecycle methods
        Logger.logPhaseStart("Service Setup Phase");
        for (IService service : sortedServices) {
            Logger.logTime(service.getClass().getSimpleName() + " Setup", () -> service.setup(this));
        }

        Logger.logPhaseStart("Service Initialization Phase");
        for (IService service : sortedServices) {
            Logger.logTime(service.getClass().getSimpleName() + " Initialization", () -> service.initialize(this));
        }

        Logger.logPhaseComplete("Service Initialization");
    }

    /**
     * Shuts down all managed services in reverse order.
     */
    public void shutdown() {
        Logger.info("--- Service Shutdown Start ---");
        if (sortedServices == null) return;

        List<IService> reversedServices = new ArrayList<>(sortedServices);
        Collections.reverse(reversedServices);

        for (IService service : reversedServices) {
            try {
                service.shutdown(this);
            } catch (Exception e) {
                Logger.logException("Failed to shutdown service " + service.getClass().getSimpleName(), e);
            }
        }
        Logger.info("--- Service Shutdown Complete ---");
    }

    /**
     * Retrieves a managed service by its interface.
     * @param serviceInterface The interface of the service to get.
     * @return An Optional containing the service instance if found.
     */
    public <T extends IService> Optional<T> getService(Class<T> serviceInterface) {
        return Optional.ofNullable(serviceInterface.cast(serviceInstances.get(serviceInterface)));
    }

    @SuppressWarnings("unchecked")
    private void instantiateService(Class<? extends IService> implClass) throws Exception {
        Class<? extends IService> providedInterface = getProvidedInterface(implClass);
        if (serviceInstances.containsKey(providedInterface)) {
            return;
        }

        if (servicesBeingConstructed.contains(implClass)) {
            throw new IllegalStateException("Cyclic Dependency Detected! Cycle involves: " + implClass.getName());
        }
        servicesBeingConstructed.add(implClass);

        Constructor<?> constructor = Arrays.stream(implClass.getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new NoSuchMethodException("No suitable constructor found for " + implClass.getSimpleName()));

        List<Object> dependencies = new ArrayList<>();
        for (Class<?> paramType : constructor.getParameterTypes()) {
            if (Alley.class.isAssignableFrom(paramType)) {
                dependencies.add(this.plugin);
                continue;
            }
            if (IService.class.isAssignableFrom(paramType)) {
                Class<? extends IService> dependencyInterface = (Class<? extends IService>) paramType;
                Class<? extends IService> dependencyImpl = this.serviceRegistry.get(dependencyInterface);
                if (dependencyImpl == null) {
                    throw new ClassNotFoundException("No implementation found for service interface: " + dependencyInterface.getName());
                }

                instantiateService(dependencyImpl);
                dependencies.add(serviceInstances.get(dependencyInterface));
            } else {
                throw new IllegalStateException("Unsupported dependency type in " + implClass.getSimpleName() + ": " + paramType.getSimpleName());
            }
        }

        IService serviceInstance = (IService) constructor.newInstance(dependencies.toArray());
        serviceInstances.put(providedInterface, serviceInstance);

        servicesBeingConstructed.remove(implClass);
    }

    @SuppressWarnings("unchecked")
    private List<Class<? extends IService>> discoverServices() {
        List<Class<? extends IService>> services = new ArrayList<>();
        try (ScanResult scanResult = new ClassGraph().acceptPackages(SERVICE_IMPL_PACKAGE).enableAnnotationInfo().scan()) {
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Service.class.getName())) {
                if (!classInfo.isAbstract() && !classInfo.isInterface()) {
                    services.add((Class<? extends IService>) classInfo.loadClass());
                }
            }
        }
        return services;
    }

    private List<Class<? extends IService>> sortServicesByPriority(List<Class<? extends IService>> services) {
        return services.stream()
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(Service.class).priority()))
                .collect(Collectors.toList());
    }

    private Class<? extends IService> getProvidedInterface(Class<? extends IService> implClass) {
        return implClass.getAnnotation(Service.class).provides();
    }
}