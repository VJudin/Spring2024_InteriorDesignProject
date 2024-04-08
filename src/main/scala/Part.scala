import javafx.scene.shape.ArcType
import scalafx.scene.paint.Color
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.shape.{Arc, Circle, Ellipse, Rectangle, Shape}
import scalafx.scene.text.FontWeight.Black
import scalafx.stage.StageStyle.Transparent

/** Alustava luokkarakenne, täytyy tehdä valmiiksi myöhemmin **/

class Part ( name: String, var width: Int, var lenght: Int, canHaveOnTop: Boolean, val shape: Shape, var x: Double, var y: Double):

  def changeSize( w: Int, l: Int) =
    width = w
    lenght = l
    this.shape.resize( w, l )

class Furniture( val fname: String,  width: Int,  lenght: Int, val canHaveOnTop: Boolean, shape: Shape, x: Double, y: Double, var color: Color, val canBePlacedOnTop: Boolean) extends Part (fname, width: Int, lenght: Int, canHaveOnTop: Boolean, shape: Shape, x: Double, y: Double):

  this.shape.fill = this.color
  
  /** Vaihtaa huonekaluolion säilyttämää väriä ja samalla muuttaa siitä mahdollisesti piirretyn version värin */
  def changeColor( c: Color) =
    color = c
    this.shape.fill = c
    
  val f = this
    
  /** Mahdollistaa tietyn huonekaluolion kopioimisen, jotta kopioita huonekaluista voi lisätä pohjapiirrustukseen*/
  def copy(): Furniture =
    var shapeOf: Shape =
      this.shape match
        case shape: Rectangle => new Rectangle:
          this.width = f.width.toDouble
          this.height = f.lenght.toDouble
        case shape: Circle => new Circle:
          radius = f.width.toDouble
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
  this.shape.fill = Color.Black


class Window( lenght: Int, x: Int, y: Int) extends Furniture( "Window", 2, lenght, false, Rectangle(2, lenght), x, y, Color.Black, false):
  this.shape.fill = this.color

