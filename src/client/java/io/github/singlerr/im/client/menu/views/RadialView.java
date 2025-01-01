package io.github.singlerr.im.client.menu.views;

import com.mojang.blaze3d.platform.InputConstants;
import icyllis.modernui.animation.ValueAnimator;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.PointF;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.View;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class RadialView extends View implements View.OnKeyListener {

  private final ValueAnimator animator;
  private final float startAngle;
  private final float angleAmount;
  private float targetAngle = 0;

  public RadialView(Context context, long duration, float startAngle, float angleAmount) {
    super(context);
    this.animator = ValueAnimator.ofFloat(0, Mth.TWO_PI);
    this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(@NotNull ValueAnimator animation) {
        targetAngle = (float) animation.getAnimatedValue();
        invalidate();
      }
    });
    animator.setDuration(duration);
    this.startAngle = startAngle;
    this.angleAmount = angleAmount;
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    animator.start();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Paint paint = Paint.obtain();
    paint.setStroke(true);
    float height = getHeight();
    float maxRadius = height * 0.4f;
    float minRadius = maxRadius - 30f;
    float centerX = getWidth() / 2f;
    float centerY = getHeight() / 2f;

    canvas.drawCircle(centerX, centerY, maxRadius, paint);
    canvas.drawCircle(centerX, centerY, minRadius, paint);

    canvas.save();
    paint.setRGBA(3, 252, 28, 255);
    paint.setStroke(false);
    canvas.drawPie(centerX, centerY, maxRadius, startAngle, angleAmount, paint);
    canvas.save();
    paint.setBlendMode(BlendMode.CLEAR);
    paint.setRGBA(0, 0, 0, 0);
    canvas.drawPie(centerX, centerY, minRadius, startAngle - 1, angleAmount + 2, paint);
    canvas.save();
    paint.setBlendMode(BlendMode.ADD);
    paint.setStrokeWidth(5);
    paint.setRGBA(255, 255, 255, 255);
    PointF t1 = new PointF(centerX, centerY);
    PointF t2 = new PointF(centerX + maxRadius * Mth.cos(targetAngle),
        centerY + maxRadius * Mth.sin(targetAngle));
    canvas.drawLine(t1, t2, paint);
    canvas.save();
    paint.recycle();
  }


  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (keyCode == InputConstants.KEY_SPACE) {

    }
    return false;
  }
}
