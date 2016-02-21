package ca.delilaheve.timetable.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

public class CalendarLayout extends RelativeLayout {

    private int rows = 1;
    private int cols = 1;

    private int rowHeight;
    private int remainderHeight;
    private int colWidth;

    private int rowHeaderWidth;

    public CalendarLayout(Context context) {
        super(context);
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public void setGrid(int rows, int cols, int h, int w) {
        this.rows = rows;
        this.cols = cols;

        rowHeight = h / rows;
        remainderHeight = h % rows;
        colWidth = (w - rowHeaderWidth) / cols;

        Resources r = getResources();
        rowHeaderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, r.getDisplayMetrics());

        System.out.println("Rows: " + rows + "  |  Cols: " + cols);
        System.out.println("Row Height: " + rowHeight + " / " + h + "  |  Col Width: " + colWidth + " / " + w);
    }

    public void addChild(View view, int row, int rowSpan, int col) {
        addChild(view, row, rowSpan, col, 1, false);
    }

    public void addHeader(View view, int row, int rowSpan, boolean isHeader) {
        addChild(view, row, rowSpan, 0, 1, isHeader);
    }

    public void addChild(View view, int row, int rowSpan, int col, int colSpan, boolean isHeader) {
        if(row > rows || row + rowSpan > rows || col > cols || col + colSpan > cols)
            return;

        // Apply Layout params
        int h, w, mTop, mLeft;

        h = rowSpan * rowHeight;
        w = colSpan * colWidth;
        mTop = row * rowHeight;

        if(!isHeader)
            mLeft = col * colWidth + rowHeaderWidth;
        else {
            mLeft = 0;
            w = rowHeaderWidth;
        }

        if(row + rowSpan == rows)
            h += remainderHeight;

        LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        params.height = h;
        params.width = w;
        params.topMargin = mTop;
        params.leftMargin = mLeft;

        view.setLayoutParams(params);

        // Add View
        addView(view);
    }

}
