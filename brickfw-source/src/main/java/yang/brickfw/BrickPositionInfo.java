package yang.brickfw;

/**
 * 组件位置信息
 */
public class BrickPositionInfo {
    int mIdxInGlobal;       // 在整个组件页中的位置
    int mIdxInGroup;        // 在上下文相同类型中的位置
    int mGroupSize;         // 附近相同类型组件的数目

    public void reset() {
        mIdxInGlobal = 0;
        mIdxInGroup = 0;
        mGroupSize = 0;
    }

    public int getIdxInGlobal() {
        return mIdxInGlobal;
    }

    public void setIdxInGlobal(int idxInGlobal) {
        mIdxInGlobal = idxInGlobal;
    }

    public int getIdxInGroup() {
        return mIdxInGroup;
    }

    public void setIdxInGroup(int idxInGroup) {
        mIdxInGroup = idxInGroup;
    }

    /**
     * 是否是一个同类型的开始，即为第一个View或之前的View与当前类型不同
     *
     * @return
     */
    public boolean isFirstInGroup() {
        return mIdxInGroup == 0;
    }

    /**
     * 是否是一个同类型的结束，即为最后一个View或之后的View与当前类型不同
     *
     * @return
     */
    public boolean isLastInGroup() {
        return mIdxInGroup + 1 == mGroupSize;
    }

    public int getGroupSize() {
        return mGroupSize;
    }

    public void setGroupSize(int groupSize) {
        mGroupSize = groupSize;
    }

    @Override
    public String toString() {
        return "BrickPositionInfo{" +
                "IdxInGlobal=" + mIdxInGlobal +
                ", IdxInGroup=" + mIdxInGroup +
                ", GroupSize=" + mGroupSize +
                '}';
    }
}
