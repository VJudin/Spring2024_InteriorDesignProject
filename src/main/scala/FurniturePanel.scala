import DesingGUI.{amountOfLamps, floorPlanScaleX, floorPlanScaleY}
import javafx.scene.input.MouseButton
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Background, Border, BorderImage, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, Pane, VBox}
import scalafx.scene.paint.Color.Pink
import scalafx.scene.shape.Shape
import scalafx.scene.text.Font
import scalafx.geometry
import scalafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.scene.shape.Shape.sfxShape2jfx

import scala.collection.mutable.ListBuffer

/** Muuttuja joka mahdollistaa lamppujen määrän seuraamisen sekä sitä kautta
 * indeksien muuttamisen, selkenee katsottaessa indexToAddTo muuttujaa. Varmistetaan
 * siis että lamput ovat kaikkien muiden huonekalujen yläpuolella käyttöliittymän näkymässä */

class FurniturePanel (f: Furniture, givenWidth: Double, givenHeight: Double, addTo: Pane, listOfFurniture: ListBuffer[Furniture] ) extends VBox:


  this.prefHeight = givenHeight
  this.prefWidth = givenWidth

  val furnitureName = new Label(f.fname):
    font = Font(14)

  /** Taustan värin, reunojen sekä spacingin ja paddingin asettelu */
  background = Background.fill(Pink)
  val stroke = BorderStroke(Color.Black, BorderStrokeStyle.Solid, CornerRadii(0), BorderWidths(5))
  border = Border( Array(stroke), Array[BorderImage]())
  spacing = 6
  padding = Insets(20, 20, 20, 20)


/** Nappi, jota painamalla huonekalu ilmestyy alustalle */
  var addButton = new Button(s"Add new ${f.fname}"):
    onAction = (event) => addFurniture()

/** Metodi, joka varsinaisesti lisää huonekalun */
  private def addFurniture(): Unit =
    /** Luodaan kopio paneelin huonekalusta ja muutetaan sen x sekä y koordinaatit,
     * jotta huonekalu ilmestyy käyttöliittymän keskelle */
    var newOne = f.copy()
    val shape = newOne.shape
    newOne.x = addTo.prefWidth.toDouble / 2
    newOne.y = addTo.prefHeight.toDouble / 2

    /** Muutuja, joka tarkistaa mihin indeksiin uusi huonekalu lisätään,
     * mahdollistaa mattojen olemisen muiden huonekalujen alla ja lamppujen olemisen
     * kaikista päällimmäisinä. Lamppujen määrä ei vaikuta pohjapiirrustuksen luomiseen
     * käytettäviin elementteihin */
    var indexToAddTo =
      f match
        case f: Rug =>
          0
        case f: Lamp =>
          amountOfLamps += 1
          addTo.children.length
        case f: Wall => 0
        case f: Door => 0
        case f: Window => 0
        case _ => addTo.children.length - amountOfLamps

    /** Lisätään uusi huonekalu kaikkien huonekalujen listaan sekä Paneen, jolloin
     * se ilmestyy käyttöliittymään */
    listOfFurniture += newOne
    addTo.children.add(indexToAddTo, shape)
    println(amountOfLamps)

    /** Skaalataan huonekalu ja sijoitetaan se oikeisiin koordinaatteihin käyttöliittymässä */
    shape.setScaleX(DesingGUI.floorPlanScaleX)
    shape.setScaleY(DesingGUI.floorPlanScaleY)
    shape.setLayoutX(newOne.x)
    shape.setLayoutY(newOne.y)

    /** Tehdään huonekalusta raahattava DraggableMaker luokan avulla */
    val draggableMaker = new DraggableMaker()
    draggableMaker.makeDraggable(newOne, listOfFurniture, addTo)

    /** Avaa muutosikkunan heti kun huonekalu lisätään kuvaan */
    popUpMaker(newOne, addTo, listOfFurniture).show()

    /** Kun uudesta huonekalusta klikataan hiiren toisella näppäimellä, ilmestyy
     * esiin muutosvalikko */
    newOne.shape.onMouseClicked = (event) =>
      if event.getButton == MouseButton.SECONDARY then
        popUpMaker(newOne, addTo, listOfFurniture).show()

  this.children = Array( furnitureName, f.shape, addButton )