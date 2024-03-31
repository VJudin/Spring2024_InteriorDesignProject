import scalafx.scene.paint.Color
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.shape.{Circle, Rectangle, Shape}

/** Alustava luokkarakenne, täytyy tehdä valmiiksi myöhemmin **/

class Part ( name: String, var width: Int, var lenght: Int, canHaveOnTop: Boolean, val shape: Shape, var x: Double, var y: Double):

  def changeSize( w: Int, l: Int) =
    width = w
    lenght = l

class Wall(width: Int, length: Int, s: Boolean,  shape: Rectangle, x: Int, y: Int) extends Part("Wall", width, length, false,  Rectangle(width, length), x, y):
  ???

class Furniture( val fname: String,  width: Int, lenght: Int, val canHaveOnTop: Boolean, shape: Shape, x: Double, y: Double, var color: Color, val canBePlacedOnTop: Boolean) extends Part (fname, width: Int, lenght: Int, canHaveOnTop: Boolean, shape: Shape, x: Double, y: Double):

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
    new Furniture( fname, width, lenght, canHaveOnTop, shapeOf, 0.0, 0.0, color, canBePlacedOnTop)
    
  def compare( second: Furniture ): Boolean = f.fname == second.fname && f.lenght == second.lenght && f.width == second.width && f.shape == second.shape && f.color == second.color && f.x == second.x && f.y == second.y
  