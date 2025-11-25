package com.comp2042;

public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    private final boolean brickLanded;

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.brickLanded = brickLanded;

    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }

     public boolean isBrickLanded() {return brickLanded;}
}
