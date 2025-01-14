package io.github.singlerr.im.client.menu.views;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.animation.ValueAnimator;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.PointF;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class RadialView extends View {

  private final ValueAnimator animator;
  private final float startAngle;
  private final float angleAmount;
  private final Consumer<Boolean> callback;
  private float targetAngle = 0;
  private boolean enabled;

  public RadialView(Context context, long duration, float startAngle, float angleAmount,
                    Consumer<Boolean> callback) {
    super(context);
    this.callback = callback;
    this.enabled = true;
    this.animator = ValueAnimator.ofFloat(0, Mth.TWO_PI);
    this.animator.addListener(new AnimatorListener() {
      @Override
      public void onAnimationEnd(Animator animation, boolean isReverse) {
        if (enabled) {
          callback.accept(false);
        }
      }
    });
    this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(@NotNull ValueAnimator animation) {
        if (!enabled) {
          return;
        }
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
    float minRadius = maxRadius - dp(30);
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
    PointF t2 = new PointF(centerX + maxRadius * Mth.cos(targetAngle - Mth.HALF_PI),
        centerY + maxRadius * Mth.sin(targetAngle - Mth.HALF_PI));
    canvas.drawLine(t1, t2, paint);
    canvas.save();
    paint.recycle();
  }


  @Override
  public boolean onGenericMotionEvent(MotionEvent event) {
    if (!enabled) {
      return false;
    }

    if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
      enabled = false;
      animator.cancel();

      float angle = (float) Math.toDegrees(targetAngle - Mth.HALF_PI);
      callback.accept(angle >= startAngle && angle <= (startAngle + angleAmount));
    }
    return false;
  }


}
