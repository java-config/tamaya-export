package org.javaconfig;

import org.javaconfig.spi.Bootstrap;
import org.javaconfig.spi.StagesSingletonSpi;

import java.util.Collection;
import java.util.Optional;

/**
 * Singleton accessor class proving access to Stage instances.
 */
final class Stages {

    /** backing singleton SPI. */
    private static final StagesSingletonSpi spi = loadStagesSingletonSpi();

    /**
     * Singleton constructor.
     */
    private Stages(){}

    /**
     * Get the default stage for develpment.
     * @return the default stage, never null.
     */
    public static Stage getDevelopmentStage(){
        return Optional.of(spi).get().getDevelopmentStage();
    }

    /**
     * Get the default stage for (component) testing.
     * @return the default stage, never null.
     */
    public static Stage getTestStage(){
        return Optional.of(spi).get().getTestStage();
    }

    /**
     * Get the default stage for integration (testing).
     * @return the default stage, never null.
     */
    public static Stage getIntegrationStage(){
        return Optional.of(spi).get().getIntegrationStage();
    }

    /**
     * Get the default stage for staging.
     * @return the default stage, never null.
     */
    public static Stage getStagingStage(){
        return Optional.of(spi).get().getStagingStage();
    }

    /**
     * Get the default stage for production.
     * @return the default stage, never null.
     */
    public static Stage getProductionStage(){
        return Optional.of(spi).get().getProductionStage();
    }


    /**
     * Method that loads the singleton backing bean from the {@link org.javaconfig.spi.Bootstrap} component.
     * @return the PropertyAdaptersSingletonSpi, never null.
     */
    private static StagesSingletonSpi loadStagesSingletonSpi(){
        return Bootstrap.getService(StagesSingletonSpi.class);
    }

    /**
     * Get a stage by name. If not present, create a new stage.
     *
     * @param name the stage's name.
     * @return tge stage instance, never null.
     */
    public static Stage getStage(String name){
        return Optional.of(spi).get().getStage(name);
    }

    /**
     * Adds a new stage.
     *
     * @param stage the new stage instance.
     * @throws java.lang.IllegalStateException if a stage with the same name is already existing.
     */
    public static void addStage(Stage stage){
        Optional.of(spi).get().addStage(stage);
    }

    /**
     * Access all the stages currently defined.
     *
     * @return the current stages, never null.
     */
    public static Collection<Stage> getStages(){
        return Optional.of(spi).get().getStages();
    }

}
