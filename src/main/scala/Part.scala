import javafx.scene
import scalafx.scene.image.Image
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.{Pink, White}
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.shape.{Arc, ArcType, Circle, Ellipse, Rectangle, Shape}

import java.io.FileInputStream

/** Yläluokka */
class Part ( name: String, var width: Double, var lenght: Double, canHaveOnTop: Boolean, val shape: Shape, var x: Double, var y: Double):

  /** Metodi joka mahdollistaa huonekalun koon muuttamisen*/
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
        shape.setType(ArcType.Round)
        shape.radiusX = w
        shape.radiusY = h
        shape.startAngle = 0.0
        shape.length = 90.0
      case _ => shape.fill = Pink

class Furniture( val fname: String,  width: Double,  lenght: Double, val canHaveOnTop: Boolean, shape: Shape, x: Double, y: Double, var color: Color, val canBePlacedOnTop: Boolean, val image: Image) extends Part (fname, width: Double, lenght: Double, canHaveOnTop: Boolean, shape: Shape, x: Double, y: Double):

  this.shape.fill = this.color
  
  /** Vaihtaa huonekaluolion säilyttämää väriä ja samalla muuttaa siitä mahdollisesti piirretyn version värin.
   * Ovien ja kaappien/tasojen väriä ei pysty vaihtamaan */
  def changeColor( c: Color) =
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
          this.setType( ArcType.Round )
          this.radiusX = f.width
          this.radiusY = f.lenght
          this.startAngle = 0.0
          this.length = 90.0
    new Furniture( fname, width, lenght, canHaveOnTop, shapeOf, 0.0, 0.0, color, canBePlacedOnTop, image)


  def compare( second: Furniture ): Boolean = f.fname == second.fname && f.lenght == second.lenght && f.width == second.width && f.shape == second.shape && f.color == second.color && f.x == second.x && f.y == second.y

class Wall(width: Int, length: Int, x: Int, y: Int, color: Color) extends Furniture("Wall", width, length, false,  Rectangle(width, length), x, y, color, false, Image( FileInputStream("src/main/Pictures/wall.jpeg")) ):
  
  this.shape.fill = this.color

class Door( width: Int, lenght: Int, x: Int, y: Int, color: Color) extends Furniture( "Door", width, lenght, false, Arc(0, 0, width, lenght , 0.0, 90.0), x, y, color, false, Image( FileInputStream("src/main/Pictures/door.jpeg"))):

  shape.fill = Color.White

  /** Jotta ovella on oikea ArcType täytyy sen sisällä tehdä match-case rakenne */
  shape match
    case shape: Arc =>
      shape.setType(scene.shape.ArcType.ROUND)
      shape.setStrokeWidth(3)
      shape.setStroke( Color.Black )
    case _ => shape.fill = Pink


class Window( width: Int, lenght: Int, x: Int, y: Int) extends Furniture( "Window", width, lenght, false, Rectangle(width, lenght), x, y, Color.Blue, false, Image(FileInputStream("src/main/Pictures/window.jpeg"))):
  this.shape.fill = this.color

class Rug( width: Double, lenght: Double, x: Double, y: Double, color: Color) extends Furniture( "Rug", width, lenght, true, Rectangle( width, lenght), x, y, color, true, Image( FileInputStream("src/main/Pictures/Rug.jpeg")))

class Lamp( width: Int, x: Double, y: Double, color: Color) extends Furniture( "Lamp", width, 1, false, Circle( width ), x, y, color, true, Image( FileInputStream("src/main/Pictures/cealingLight.jpeg")))

class Counter( width: Int, lenght: Int, x: Int, y: Int, color: Color) extends Furniture("Counter", width, lenght, false, Rectangle( width, lenght), x, y, color, false, Image( FileInputStream("src/main/Pictures/counter.jpeg"))):

  shape.fill = Color.White
  shape.setStrokeWidth(5)
  shape.setStroke( Color.Black )