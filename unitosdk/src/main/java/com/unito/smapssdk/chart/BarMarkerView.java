package com.unito.smapssdk.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.unito.smapssdk.R;

public class BarMarkerView extends MarkerView {
    public static final int ARROW_SIZE = 40; // 箭头的大小
    private static final float CIRCLE_OFFSET = 0;//因为我这里的折点是圆圈，所以要偏移，防止直接指向了圆心
    private static final float STOKE_WIDTH = 0;//这里对于stroke_width的宽度也要做一定偏移
    private int STROKE_COLOR = 0xffffffff;//指示器默认的颜色

    private final TextView tvContent;
    private RelativeLayout relative;
    private final TextView tvContent2;
    IndexAxisValueFormatter formatter;
    int index;

    public BarMarkerView(Context context, int layoutResource,IndexAxisValueFormatter formatter) {
        super(context, layoutResource);
        this.formatter = formatter;
        tvContent = findViewById(R.id.tvContent);
        tvContent2 = findViewById(R.id.tvContent2);
        relative = findViewById(R.id.relative);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
//            CandleEntry ce = (CandleEntry) e;
////            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
//            tvContent.setText(ce.getY()+"kWh");
        } else {
//            tvContent.setText(Utils.formatNumber(e.getY(), 0, tru e));
            index = highlight.getDataSetIndex();
//            Log.e("refreshContent-->",e.getX()+"  "+highlight.getDataIndex()+"  "+highlight.getDataSetIndex()+"  "+highlight.getX()+"  "+highlight.getStackIndex());
            if (index == 1) {
                tvContent.setTextColor(Color.rgb(0, 132, 255));
                tvContent2.setTextColor(Color.rgb(0, 132, 255));
                tvContent.setText("⦁ "+formatter.getFormattedValue((float) (Math.floor(e.getX())+0.31)));
            } else {
                tvContent.setTextColor(Color.rgb(255, 78, 0));
                tvContent2.setTextColor(Color.rgb(255, 78, 0));
                tvContent.setText("⦁ "+formatter.getFormattedValue(e.getX()));
            }
            tvContent2.setText(e.getY()+"kWh");

        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        MPPointF offset = getOffset();
        Chart chart = getChartView();
        float width = getWidth();
        float height = getHeight();
// posY \posX 指的是markerView左上角点在图表上面的位置
//处理Y方向
        if (posY <= height + ARROW_SIZE) {// 如果点y坐标小于markerView的高度，如果不处理会超出上边界，处理了之后这时候箭头是向上的，我们需要把图标下移一个箭头的大小
            offset.y = ARROW_SIZE;
        } else {//否则属于正常情况，因为我们默认是箭头朝下，然后正常偏移就是，需要向上偏移markerView高度和arrow size，再加一个stroke的宽度，因为你需要看到对话框的上面的边框
            offset.y = -height - ARROW_SIZE - STOKE_WIDTH; // 40 arrow height   5 stroke width
        }
//处理X方向，分为3种情况，1、在图表左边 2、在图表中间 3、在图表右边
//
        if (posX > chart.getWidth() - width) {//如果超过右边界，则向左偏移markerView的宽度
            offset.x = -width;
        } else {//默认情况，不偏移（因为是点是在左上角）
            offset.x = 0;
            if (posX > width / 2) {//如果大于markerView的一半，说明箭头在中间，所以向右偏移一半宽度
                offset.x = -(width / 2);
            }
        }
        return offset;
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
//        if (BLEConstant.isLavaDevice()) {
//            if (index == 1) {
//                return;
//            }
//        } else if (BLEConstant.isSparkleDevice()) {
//            if (index != 1) {
//                return;
//            }
//        }
        Paint paint = new Paint();//绘制边框的画笔
        paint.setStrokeWidth(STOKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setStrokeJoin(Paint.Join.ROUND);
        if (index == 1) {
            paint.setColor(Color.rgb(0, 132, 255));
        } else {
            paint.setColor(Color.rgb(255, 78, 0));
        }

        Paint whitePaint = new Paint();//绘制底色白色的画笔
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);

        Chart chart = getChartView();
        float width = getWidth();
        float height = getHeight();

        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);
        int saveId = canvas.save();

        Path path = new Path();
        if (posY < height + ARROW_SIZE) {//处理超过上边界
            path = new Path();
            path.moveTo(0, 0);
            if (posX > chart.getWidth() - width) {//超过右边界
                path.lineTo(width - ARROW_SIZE, 0);
                path.lineTo(width, -ARROW_SIZE + CIRCLE_OFFSET);
                path.lineTo(width, 0);
            } else {
                if (posX > width / 2) {//在图表中间
                    path.lineTo(width / 2 - ARROW_SIZE / 2, 0);
                    path.lineTo(width / 2, -ARROW_SIZE + CIRCLE_OFFSET);
                    path.lineTo(width / 2 + ARROW_SIZE / 2, 0);
                } else {//超过左边界
                    path.lineTo(0, -ARROW_SIZE + CIRCLE_OFFSET);
                    path.lineTo(0 + ARROW_SIZE, 0);
                }
            }
            path.lineTo(0 + width, 0);
            path.lineTo(0 + width, 0 + height);
            path.lineTo(0, 0 + height);
            path.lineTo(0, 0);
            path.offset(posX + offset.x, posY + offset.y);
        } else {//没有超过上边界
            path = new Path();
            path.moveTo(0, 0);
            path.lineTo(0 + width, 0);
            path.lineTo(0 + width, 0 + height);
            if (posX > chart.getWidth() - width) {
                path.lineTo(width, height + ARROW_SIZE - CIRCLE_OFFSET);
                path.lineTo(width - ARROW_SIZE, 0 + height);
                path.lineTo(0, 0 + height);
            } else {
                if (posX > width / 2) {
                    path.lineTo(width / 2 + ARROW_SIZE / 2, 0 + height);
                    path.lineTo(width / 2, height + ARROW_SIZE - CIRCLE_OFFSET);
                    path.lineTo(width / 2 - ARROW_SIZE / 2, 0 + height);
                    path.lineTo(0, 0 + height);
                } else {
                    path.lineTo(0 + ARROW_SIZE, 0 + height);
                    path.lineTo(0, height + ARROW_SIZE - CIRCLE_OFFSET);
                    path.lineTo(0, 0 + height);
                }
            }
            path.lineTo(0, 0);
            path.offset(posX + offset.x, posY + offset.y);
        }

        // translate to the correct position and draw
        canvas.drawPath(path, whitePaint);
        canvas.drawPath(path, paint);
        canvas.translate(posX + offset.x, posY + offset.y);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }
}