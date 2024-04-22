
import scalafx.scene.paint.Color
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.shape.{Arc, ArcType, Circle, Ellipse, Rectangle, Shape}

/** Alustava luokkarakenne, täytyy tehdä valmiiksi myöhemmin **/

class Part ( name: String, var width: Double, var lenght: Double, canHaveOnTop: Boolean, val shape: Shape, var x: Double, var y: Double):

  def changeSize( w: Double, l: Double) =
    width = w
    lenght = l
    this.shape match
      case shape: Rectangle =>
        shape.width = w
        shape.height = h
      case shape: Circle =>
        shape.radius = w
      case shape: Ellipse =>
        shape.radiusX = w
        shape.radiusY = h
      case shape: Arc =>
        shape.setType(ArcType.ROUND)
        shape.radiusX = w
        shape.radiusY = h
        shape.startAngle = 0.0
        shape.length = 90.0

class Furniture( val fname: String,  width: Double,  lenght: Double, val canHaveOnTop: Boolean, shape: Shape, x: Double, y: Double, var color: Color, val canBePlacedOnTop: Boolean) extends Part (fname, width: Double, lenght: Double, canHaveOnTop: Boolean, shape: Shape, x: Double, y: Double):

  this.shape.fill = this.color
  
  /** Vaihtaa huonekaluolion säilyttämää väriä ja samalla muuttaa siitä mahdollisesti piirretyn version värin */
  def changeColor( c: Color) =
    shape match
      case shape: Arc =>
        shape.strokeWidth = 3
        shape.stroke = Color.Black
        shape.fill = Color.White
      case _ =>
        color = c
        this.shape.fill = c
    
  val f = this

    
  /** Mahdollistaa tietyn huonekaluolion kopioimisen, jotta kopioita huonekaluista voi lisätä pohjapiirrustukseen*/
  def copy(): Furniture =
    var shapeOf: Shape =
      this.shape match
        case shape: Rectangle => new Rectangle:
          this.width = f.width
          this.height = f.lenght
        case shape: Circle => new Circle:
          radius = f.width
        case  shape: Ellipse => new Ellipse:
          this.radiusX = f.width
          this.radiusY = f.lenght
        case shape: Arc => new Arc:
          this.setType( ArcType.ROUND )
          this.radiusX = f.width
          this.radiusY = f.lenght
          this.startAngle = 0.0
          this.length = 90.0
    new Furniture( fname, width, lenght, canHaveOnTop, shapeOf, 0.0, 0.0, color, canBePlacedOnTop)


  def compare( second: Furniture ): Boolean = f.fname == second.fname && f.lenght == second.lenght && f.width == second.width && f.shape == second.shape && f.color == second.color && f.x == second.x && f.y == second.y

class Wall(width: Int, length: Int, x: Int, y: Int, color: Color) extends Furniture("Wall", width, length, false,  Rectangle(width, length), x, y, color, false ):
  this.shape.fill = this.color

class Door( width: Int, lenght: Int, x: Int, y: Int, color: Color) extends Furniture( "Door", width, lenght, false, Arc(0, 0, width, lenght , 0.0, 90.0), x, y, color, false):

  shape.fill = Color.White

  shape match
    case shape: Arc =>
      shape.setType(ArcType.ROUND)
      shape.setStrokeWidth(3)
      shape.setStroke( Color.Black )


class Window( width: Int, lenght: Int, x: Int, y: Int) extends Furniture( "Window", width, lenght, false, Rectangle(width, lenght), x, y, Color.Blue, false):
  this.shape.fill = this.color

class Rug( width: Double, lenght: Double, x: Double, y: Double, color: Color) extends Furniture( "Rug", width, lenght, true, Rectangle( width, lenght), x, y, color, true)

class Lamp( width: Int, x: Double, y: Double, color: Color) extends Furniture( "Lamp", width, 1, false, Circle( width ), x, y, color, true)

