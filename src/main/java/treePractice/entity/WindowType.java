package treePractice.entity;

/**
 * Created by wumk124866 on 2018/6/20.
 */
public class WindowType {

    public static final WindowType WINDOW_NOTIFICATION = new WindowType("notification");
    public static final WindowType WINDOW_CONFIRM = new WindowType("confirm");
    private final String windowType;

    public WindowType(String windowType) {
        this.windowType = windowType;
    }

    public String getWindowType() {
        return windowType;
    }
}
