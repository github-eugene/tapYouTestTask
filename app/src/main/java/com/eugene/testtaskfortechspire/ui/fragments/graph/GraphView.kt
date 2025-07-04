package com.eugene.testtaskfortechspire.ui.fragments.graph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.eugene.testtaskfortechspire.R
import com.eugene.testtaskfortechspire.model.PointUiModel
import androidx.core.graphics.withTranslation
import com.eugene.testtaskfortechspire.ui.utils.color
import com.eugene.testtaskfortechspire.ui.utils.string

class GraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs),
    ScaleGestureDetector.OnScaleGestureListener,
    GestureDetector.OnGestureListener {

    private var points: List<PointUiModel> = emptyList()
    private var isSmoothLine: Boolean = false

    private val labelX = context.getString(R.string.value_x)
    private val labelY = context.getString(R.string.value_y)

    private var minX = 0f
    private var maxX = 0f
    private var minY = 0f
    private var maxY = 0f

    private var originX = 0f
    private var originY = 0f

    private var scaleX = 1f
    private var scaleY = 1f

    private var mappedPoints: List<Pair<Float, Float>> = emptyList()

    private var axisStrokeWidth = 2f
    private var lineStrokeWidth = 4f
    private var pointsTextSize = 24f
    private var pointRadius = 8f
    private var coordinateTextOffset = 10f

    private val axisPaint = Paint().apply {
        color = color(R.color.purple_700)
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        color = color(R.color.purple_200)
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val pointPaint = Paint().apply {
        color = color(R.color.purple_500)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = color(R.color.black)
        isAntiAlias = true
    }

    private val path = Path()

    private val scaleDetector = ScaleGestureDetector(context, this)
    private val gestureDetector = GestureDetector(context, this)

    private var scaleFactor = 1f
    private var translateX = 0f
    private var translateY = 0f

    private val minScale = 0.5f
    private val maxScale = 8f

    fun setPoints(points: List<PointUiModel>, isSmoothLine: Boolean = false) {
        this.points = points.sortedBy { it.x }
        this.isSmoothLine = isSmoothLine

        if (points.isEmpty()) return

        minX = points.minOf { it.x }
        maxX = points.maxOf { it.x }
        minY = points.minOf { it.y }
        maxY = points.maxOf { it.y }

        adaptStyleForPointsCount()
        resetView()
        updateMappedPoints()
        invalidate()
    }

    private fun adaptStyleForPointsCount() {
        val count = points.size
        when {
            count <= 50 -> {
                axisStrokeWidth = 4f
                lineStrokeWidth = 6f
                pointsTextSize = 20f
                pointRadius = 8f
            }
            count <= 100 -> {
                axisStrokeWidth = 3f
                lineStrokeWidth = 4f
                pointsTextSize = 16f
                pointRadius = 6f
            }
            count <= 500 -> {
                axisStrokeWidth = 2f
                lineStrokeWidth = 1f
                pointsTextSize = 10f
                pointRadius = 4f
            }
            else -> {
                axisStrokeWidth = 1f
                lineStrokeWidth = 0.5f
                pointsTextSize = 6f
                pointRadius = 1.5f
            }
        }
        axisPaint.strokeWidth = axisStrokeWidth
        linePaint.strokeWidth = lineStrokeWidth
        textPaint.textSize = pointsTextSize
    }

    private fun resetView() {
        scaleFactor = 1f
        translateX = 0f
        translateY = 0f
    }

    private fun updateMappedPoints() {
        if (points.isEmpty() || width == 0 || height == 0) return

        val padding = 60f
        val scaledWidth = (width - 2 * padding)
        val scaledHeight = (height - 2 * padding)

        scaleX = scaledWidth / (maxX - minX)
        scaleY = scaledHeight / (maxY - minY)

        originX = padding + (-minX) * scaleX
        originY = height - padding - (-minY) * scaleY

        mappedPoints = points.map { point ->
            Pair(
                originX + point.x * scaleX,
                originY - point.y * scaleY
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (points.isEmpty()) return

        canvas.withTranslation(translateX, translateY) {
            scale(scaleFactor, scaleFactor, width / 2f, height / 2f)

            drawLine(0f, originY, width.toFloat(), originY, axisPaint)
            drawLine(originX, 0f, originX, height.toFloat(), axisPaint)

            drawText(
                labelX,
                width - 50f,
                originY - 20f,
                textPaint
            )
            drawText(
                labelY ,
                originX + 20f,
                40f,
                textPaint
            )

            path.reset()
            if (isSmoothLine && mappedPoints.size > 2) {
                path.moveTo(mappedPoints[0].first, mappedPoints[0].second)
                for (i in 0 until mappedPoints.size - 1) {
                    val current = mappedPoints[i]
                    val next = mappedPoints[i + 1]
                    val controlX1 = (current.first + next.first) / 2
                    val controlY1 = current.second
                    val controlX2 = (current.first + next.first) / 2
                    val controlY2 = next.second
                    path.cubicTo(
                        controlX1, controlY1,
                        controlX2, controlY2,
                        next.first, next.second
                    )
                }
            } else {
                mappedPoints.forEachIndexed { index, (x, y) ->
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }
            }
            drawPath(path, linePaint)

            mappedPoints.forEachIndexed { index, (x, y) ->
                drawCircle(x, y, pointRadius, pointPaint)
                val point = points[index]
                drawText(
                    "(${point.x.toInt()},${point.y.toInt()})",
                    x + coordinateTextOffset,
                    y - coordinateTextOffset,
                    textPaint
                )
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateMappedPoints()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> parent?.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_MOVE -> {
                if (scaleDetector.isInProgress) {
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent?.requestDisallowInterceptTouchEvent(false)
        }
        var handled = scaleDetector.onTouchEvent(event)
        handled = gestureDetector.onTouchEvent(event) || handled
        return handled || super.onTouchEvent(event)
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor *= detector.scaleFactor
        scaleFactor = scaleFactor.coerceIn(minScale, maxScale)
        invalidate()
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean = true
    override fun onScaleEnd(detector: ScaleGestureDetector) {}

    override fun onDown(e: MotionEvent): Boolean = true
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        translateX -= distanceX
        translateY -= distanceY
        invalidate()
        return true
    }

    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean = false
    override fun onLongPress(e: MotionEvent) {}
    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean = false
}