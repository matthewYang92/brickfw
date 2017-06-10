package yang.brickfw;

/**
 * author: Matthew Yang on 17/6/10
 * e-mail: yangtian@yy.com
 */

public interface IBrickEventHandler {
    void handleBrickEvent(int eventType, Object... params);
}
