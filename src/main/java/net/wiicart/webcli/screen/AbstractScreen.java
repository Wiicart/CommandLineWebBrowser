package net.wiicart.webcli.screen;

import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScreen<T extends AbstractScreen<T>> {

    public abstract ScreenFutureRunner<T> show();

    /**
     * Tells this class that any further Runnables with {@link AbstractScreen#then(Runnable)}
     * may be invoked immediately.
     * @return A boolean
     */
    //todo remove, uneccessarry
    abstract boolean readyToExecute();

    /**
     * Class that manages Future Runnables to be executed once a Page has served its purpose.
     * @param <T> The type this was obtained from
     */
    public static final class ScreenFutureRunner<T extends AbstractScreen<T>> {

        private final T screen;

        private final List<Runnable> runnables = new ArrayList<>();

        ScreenFutureRunner(T screen) {
            this.screen = screen;
        }

        public final ScreenFutureRunner<T> then(Runnable runnable) {
            if (screen.readyToExecute()) {
                runnable.run();
            } else {
                runnables.add(runnable);
            }
            return this;
        }

        /**
         * Invokes all Runnables collected from {@link AbstractScreen#then(Runnable)}
         * This must be called once the Screen is in an acceptable state to run the Runnables.
         */
        @MustBeInvokedByOverriders
        void executeRunnables() {
            for (Runnable runnable : runnables) {
                runnable.run();
            }
            runnables.clear();
        }
    }

}
