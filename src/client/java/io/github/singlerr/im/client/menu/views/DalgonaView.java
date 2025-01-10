package io.github.singlerr.im.client.menu.views;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import io.github.singlerr.im.client.sounds.LoopSoundInstance;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;

@Slf4j
public class DalgonaView extends View implements View.OnGenericMotionListener {

  private final Rect imageSize;
  private final Bitmap original;
  private final Bitmap buffer;
  private final float[] killColor;
  private final float[] targetColor;
  private final float threshold;
  private final int killCount;
  private final SoundInstance scratchSound;
  private final Consumer<Boolean> callback;
  private Image image;
  private boolean dragging = false;
  private int currentKillCount;
  private boolean completed = false;

  public DalgonaView(Context context, SoundEvent scratchSound, Bitmap original, Bitmap buffer,
                     float[] killColor,
                     float[] targetColor, int threshold, int killCount,
                     Consumer<Boolean> callback) {
    super(context);
    this.image = Image.createTextureFromBitmap(original);
    this.original = original;
    this.buffer = buffer;
    this.imageSize = new Rect(0, 0, image.getWidth(), image.getHeight());
    this.killColor = killColor;
    this.targetColor = targetColor;
    this.threshold = threshold;
    this.killCount = killCount;
    this.currentKillCount = 0;
    this.scratchSound = new LoopSoundInstance(scratchSound);
    this.callback = callback;
  }


  private float distance(float[] a, float[] b) {
    int rmean = (int) (a[0] + b[0]) / 2;
    int r_ = (int) (a[0] - b[0]);
    int g_ = (int) (a[1] - b[1]);
    int b_ = (int) (a[2] - b[2]);
    return (float) Math.sqrt(
        (((512 + rmean) * r_ * r_) >> 8) + 4 * g_ * g_ + (((767 - rmean) * b_ * b_) >> 8));
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    setOnGenericMotionListener(this);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Paint paint = Paint.obtain();
    int centerX = getWidth() / 2;
    int centerY = getHeight() / 2;

    int left = centerX - getHeight() / 2;
    Rect targetSize = new Rect(left, getTop(), left + getHeight(), getBottom());
    float radius = (targetSize.height() / 2f) * 1.1f;
    paint.setRGBA(107, 104, 102, 255);
    canvas.drawCircle(centerX, centerY, radius, paint);

    if (!completed) {
      canvas.drawImage(image, imageSize, targetSize, paint);
      canvas.save();
    }
    paint.recycle();
  }

  public void onDrag(double mouseX, double mouseY) {
    if (completed) {
      return;
    }
    int centerX = getWidth() / 2;
    int left = centerX - getHeight() / 2;
    mouseX -= left;
    mouseY -= getTop();
    if (mouseX < 0 || mouseY < 0) {
      return;
    }
    double relativeY = (mouseY / getHeight()) * original.getHeight();
    double relativeX = (mouseX / getHeight()) * original.getWidth();
    if (relativeX >= original.getWidth() || relativeY >= original.getHeight()) {
      return;
    }
    if (checkKill(relativeX, relativeY)) {
      completed = true;
      callback.accept(false);
      Minecraft.getInstance().getSoundManager().stop(scratchSound);
    }
    buffer.setColor4f((int) relativeX, (int) relativeY, new float[] {0, 0, 0, 0});
  }

  private boolean checkKill(double x, double y) {
    float[] t = new float[4];
    original.getColor4f((int) x, (int) y, t);
    t = new float[] {t[0] * 255, t[1] * 255, t[2] * 255, t[3] * 255};
    float d = distance(killColor, t);
    if (d < 10) {
      currentKillCount++;
    }
    return currentKillCount >= killCount;
  }

  private float getTone(float[] color) {
    return 0.2126f * color[0] + 0.7152f * color[1] + 0.0722f * color[2];
  }

  private boolean checkCompletion(Bitmap bitmap) {
    int count = 0;
    for (int x = 0; x < bitmap.getWidth(); x++) {
      for (int y = 0; y < bitmap.getHeight(); y++) {
        float[] color = new float[4];
        bitmap.getColor4f(x, y, color);
        float alpha = color[3];
        if (alpha < 1) {
          continue;
        }
        float tone = getTone(new float[] {color[0] * 255, color[1] * 255, color[2] * 255});
        if (tone < 50) {
          count++;
        }
      }
    }
    System.out.println(count);
    return count <= threshold;
  }

  @Override
  public boolean onGenericMotion(View v, MotionEvent event) {
    if (event.getActionButton() == 0 && event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
      if (dragging) {
        onDrag(event.getX(), event.getY());
      }
    } else if (event.getActionButton() == 1 &&
        event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
      dragging = true;
      Minecraft.getInstance().getSoundManager().play(scratchSound);
    } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
      Minecraft.getInstance().getSoundManager().stop(scratchSound);
      dragging = false;
      image = Image.createTextureFromBitmap(buffer);
      if (checkCompletion(buffer)) {
        completed = true;
        callback.accept(true);
        Minecraft.getInstance().getSoundManager().stop(scratchSound);
      }
      invalidate();
    }

    return false;
  }
}
