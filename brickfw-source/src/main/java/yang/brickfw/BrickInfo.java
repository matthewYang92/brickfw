package yang.brickfw;


/**
 * 模块的数据实体类
 */
public class BrickInfo {

    private String mType;                   // 组件类型

    private Object mExtra;                  // 本地字段，用于存储不同组件设置的个性数据

    private int mColumns = 1;

    public BrickInfo(String type) {
        mType = type;
    }

    public BrickInfo(BrickInfo brickInfo) {
        set(brickInfo);
    }

    public BrickInfo(String type, Object extra) {
        mType = type;
        mExtra = extra;
    }

    public BrickInfo(String type, Object extra, int columns) {
        mType = type;
        mExtra = extra;
        mColumns = columns;
    }


    public void set(BrickInfo brickInfo) {
        this.mType = brickInfo.getType();
        mExtra = brickInfo.getExtra();
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    public Object getExtra() {
        return mExtra;
    }

    public void setExtra(Object mExtra) {
        this.mExtra = mExtra;
    }

    public int getColumns() {
        return mColumns;
    }

    public void setColumns(int mColumns) {
        this.mColumns = mColumns;
    }

    @Override public String toString() {
        return "BrickInfo{" + "mType='" + mType + '\'' + ", mExtra=" + mExtra + '}';
    }
}
