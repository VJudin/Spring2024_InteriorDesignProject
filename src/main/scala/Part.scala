import scalafx.scene.paint.Color
import scalafx.scene.shape.{Rectangle, Shape}

/** Alustava luokkarakenne, täytyy tehdä valmiiksi myöhemmin **/

class Part ( var width: Int, var lenght: Int, canHaveOnTop: Boolean, shape: Shape, x: Int, y: Int):

  def changeSize( w: Int, l: Int) =
    width = w
    lenght = l

class Wall(width: Int, length: Int, s: Boolean,  shape: Rectangle, x: Int, y: Int) extends Part(width, length, false,  Rectangle(width, length), x, y):
  ???

class Furniture( width: Int, lenght: Int, canHaveOnTop: Boolean, shape: Shape, x: Int, y: Int, var color: Color, canBePlacedOnTop: Boolean) extends Part (width: Int, lenght: Int, canHaveOnTop: Boolean, shape: Shape, x: Int, y: Int):

  def changeColor( c: Color) =
    color = c