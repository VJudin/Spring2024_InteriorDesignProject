import javafx.scene
import scalafx.scene.image.Image
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.{Pink, White}
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.shape.{Arc, ArcType, Circle, Ellipse, Rectangle, Shape}

import java.io.FileInputStream

class Furniture(
  val fname: String,
  var width: Double,
  var lenght: Double,
  val canHaveOnTop: Boolean,
  val shape: Shape,
  var x: Double,
  var y: Double,
  var color: Color,
  val canBePlacedOnTop: Boolean,
  val image: Image
):

  this.shape.fill = this.color

  /** Method that allows for the size of the furniture to change
    */
  def changeSize(w: Double, l: Double): Unit =
    width = w
    lenght = l
    this.shape match
      case shape: Rectangle =>
        shape.width = w
        shape.height = l
      case shape: Circle =>
        shape.radius = w / 2
      case shape: Ellipse =>
        shape.radiusX = w / 2
        shape.radiusY = l / 2
      case shape: Arc =>
        shape.setType(ArcType.Round)
        shape.radiusX = w
        shape.radiusY = w
        shape.startAngle = 0.0
        shape.length = 90.0
      case _ => shape.fill = Pink

  /** Changes the color of the furniture. The color of doors and counters can't
    * be changed.
    */
  def changeColor(c: Color): Unit =
    shape match
      case shape: Arc =>
        shape.strokeWidth = 3
        shape.stroke = Color.Black
        shape.fill = Color.White
      case shape: Rectangle =>
        if this.fname == "Counter" then
          shape.strokeWidth = 5
          shape.stroke = Color.Black
          shape.fill = Color.White
        else
          color = c
          shape.fill = c
      case _ =>
        color = c
        shape.fill = c

  val f = this

  /** Copies a piece of furniture which can then be added to the floorplan */
  def copy(): Furniture =
    var shapeOf: Shape =
      this.shape match
        case shape: Rectangle =>
          new Rectangle:
            this.width = f.width
            this.height = f.lenght
        case shape: Circle =>
          new Circle:
            radius = f.width
        case shape: Ellipse =>
          new Ellipse:
            this.radiusX = f.width
            this.radiusY = f.lenght
        case shape: Arc =>
          new Arc:
            this.setType(ArcType.Round)
            this.radiusX = f.width
            this.radiusY = f.lenght
            this.startAngle = 0.0
            this.length = 90.0
    new Furniture(
      fname,
      width,
      lenght,
      canHaveOnTop,
      shapeOf,
      0.0,
      0.0,
      color,
      canBePlacedOnTop,
      image
    )

class Wall(width: Int, length: Int, x: Int, y: Int, color: Color)
    extends Furniture(
      "Wall",
      width,
      length,
      false,
      Rectangle(width, length),
      x,
      y,
      color,
      false,
      Image(FileInputStream("src/main/Pictures/wall.jpeg"))
    ):

  this.shape.fill = this.color

class Door(width: Int, lenght: Int, x: Int, y: Int, color: Color)
    extends Furniture(
      "Door",
      width,
      lenght,
      false,
      Arc(0, 0, width, lenght, 0.0, 90.0),
      x,
      y,
      color,
      false,
      Image(FileInputStream("src/main/Pictures/door.jpeg"))
    ):

  shape.fill = Color.White

  /** Match-case makes sure a door has the correct Arc type
    */
  shape match
    case shape: Arc =>
      shape.setType(scene.shape.ArcType.ROUND)
      shape.setStrokeWidth(3)
      shape.setStroke(Color.Black)
    case _ => shape.fill = Pink

class Window(width: Int, lenght: Int, x: Int, y: Int)
    extends Furniture(
      "Window",
      width,
      lenght,
      false,
      Rectangle(width, lenght),
      x,
      y,
      Color.Blue,
      false,
      Image(FileInputStream("src/main/Pictures/window.jpeg"))
    ):
  this.shape.fill = this.color

class Rug(
  width: Double,
  lenght: Double,
  x: Double,
  y: Double,
  shape: Shape,
  color: Color,
  image: Image
) extends Furniture("Rug", width, lenght, true, shape, x, y, color, true, image)

class Lamp(width: Int, x: Double, y: Double, color: Color)
    extends Furniture(
      "Lamp",
      width,
      1,
      false,
      Circle(width),
      x,
      y,
      color,
      true,
      Image(FileInputStream("src/main/Pictures/cealingLight.jpeg"))
    )

class Counter(width: Int, lenght: Int, x: Int, y: Int, color: Color)
    extends Furniture(
      "Counter",
      width,
      lenght,
      false,
      Rectangle(width, lenght),
      x,
      y,
      color,
      false,
      Image(FileInputStream("src/main/Pictures/counter.jpeg"))
    ):

  shape.fill = Color.White
  shape.setStrokeWidth(5)
  shape.setStroke(Color.Black)
