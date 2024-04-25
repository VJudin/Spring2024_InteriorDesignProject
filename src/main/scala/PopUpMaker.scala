import DesingGUI.amountOfLamps
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ColorPicker, Label, Spinner}
import scalafx.scene.layout.{GridPane, HBox, Pane, VBox}
import scalafx.scene.shape.{Arc, Circle, Rectangle}
import scalafx.scene.transform.Rotate
import scalafx.stage.Stage
import scalafx.scene.SceneIncludes.jfxColor2sfx
import scalafx.scene.image.ImageView

import scala.collection.mutable.ListBuffer


class PopUpMaker:
  
  /** Metodi, joka tuottaa popUp ikkunan joka mahdollistaa huonekalun ominaisuuksien muuttamisen */
  def makePopUp(n: Furniture, furnitureIsIn: Pane, listOfFurniture: ListBuffer[Furniture] ): Stage =
  
    val stage = new Stage()
    stage.setX( 200 )
    stage.setY( 200 )
  
  
    var w = 0.0
    var h = 0.0
  
    /** Tarkistetaan, onko huonekalun leveys vai pituus suurempi. Arvoa käytetään myöhemmin
     * ikkunan ulkonäön siisteyden takaamiseksi. */
    val bigger =
      if n.width < n.lenght then
        n.lenght
      else
        n.width
  
  
   /** Luodaan uusi GradPane, johon lisätään kaikki popUpin elementit */
    val grid = new GridPane():
      hgap = 10
      vgap = 10
      padding = Insets(10, 10, 10, 10)
  
  
    /** Paneeli, jossa säilytetään huonekalun muutokseen liittyviä kenttiä */
    val changePanel = new VBox():
      spacing = 20
  
  
    /** Paneeli, joka säilytää submit ja delete napit */
    val submitOrDelete = new HBox():
      spacing = 20
      padding = Insets(20, 20, 20, 20)
  
  
    /** Paneeli, joka säilyttää kopion huonekalun muodosta */
    val shapePanel = new VBox():
      val a = bigger / 2
      padding = Insets( a, a ,a, a )
  
  
    val label1 = new Label("Modify this piece of furniture")
  
  
    /** Kopioidaan huonekalu ja asetetaan sille kaikki alkuperäisen huonekalun ominaisuudet */
    val copyOfFurniture = n.copy()
  
    copyOfFurniture.changeColor( n.color )
  
    val shape = copyOfFurniture.shape
  
    shape.getTransforms.addAll( n.shape.getTransforms )
  
  
    /** Alustetaan ColorPicker ja se, että esimerkkihuonekalun väri vaihtuu, kun ColorPickerin arvoa muutetaan*/
    val colorPicker = ColorPicker( n.color )
  
    colorPicker.onAction = (event) => copyOfFurniture.changeColor(colorPicker.getValue)
  
  
  /** Luodaan spinnerit ja yksiköt, sekä HBoxit, jotta nämä saadaan näkymään vierekkäin */
    val scaleWidth = new Spinner[Double](10.0, 500, n.width, 10.0)
  
    val scaleHeight = new Spinner[Double](10.0, 500, n.lenght, 10.0)
  
    val label2 = new Label("cm")
  
    val label3 = new Label("cm")
  
    val scalingWidth = new HBox():
      spacing = 5
      children = Array(scaleWidth, label2)
  
    val scalingHeight = new HBox():
      spacing = 5
      children = Array(scaleHeight, label3)
  
  
  /** Nappi, joka saa muutoksen näkyviin pohjapiirustuksessa sijaitsevaan huonekaluun */
    val submitButton = new Button( "Submit changes"):
  
      onAction = (event) =>
        n.changeColor( colorPicker.getValue )
        n.shape.getTransforms.clear()
        n.shape.getTransforms.addAll( copyOfFurniture.shape.getTransforms )
        w = scaleWidth.getValue
        h = scaleHeight.getValue
        n.changeSize(scaleWidth.getValue, scaleHeight.getValue)
        stage.close()
  
  
  /** Nappi joka mahdollistaa huonekalun poistamisen */
    val deleteButton = new Button( "Delete this furniture"):
      onAction = (event) =>
        var indexOf = listOfFurniture.indexOf(n)
        listOfFurniture.remove(indexOf)
        furnitureIsIn.children.remove( n.shape )
        if n.fname == "Lamp" then
          amountOfLamps -= 1
        stage.close()
  
  /** Nappi jota painamalla huonekalu kääntyy. Tietyt huonekalut eivät automaattisesti käänny keskikohdastaan,
   * joten niille on laskettu keskikohta */
    val rotateButton = new Button( "Rotate this furniture"):
      onAction = (event) =>
        n.shape match
          case shape: Rectangle =>
            copyOfFurniture.shape.getTransforms.add( new Rotate( 45, n.width / 2, n.lenght / 2 ))
          case shape: Arc =>
            copyOfFurniture.shape.getTransforms.add( new Rotate( 45, n.width / 2, -n.width / 2  ))
          case _ =>
            copyOfFurniture.shape.getTransforms.add( new Rotate( 45, 0, 0 ) )
  
  
  /** Kaikilla huonekaluilla ei voi muuttaa kaikkia attribuutteja. Tässä match-case rakenteessa
   * changePanel saa oikeat lapset riippuen siitä, mikä muto on kyseessä. If else haarassa
   * käydään läpi, minkä suorakaiteen muotoisten huonekalujen mitä attribuutteja voi muuttaa */
    n.shape match
      case shape: Circle =>
        changePanel.children = Array( colorPicker, scalingWidth)
      case shape: Arc =>
        changePanel.children = Array( rotateButton, scalingWidth, scalingHeight)
      case _ =>
        if n.fname == "Counter" then
          changePanel.children = Array( rotateButton, scalingWidth, scalingHeight)
        else if n.fname == "Wall" then
          changePanel.children = Array( rotateButton, scalingWidth, scalingHeight)
        else if n.fname == "Window" then
          changePanel.children = Array( rotateButton, scalingWidth, scalingHeight)
        else
          changePanel.children = Array( colorPicker, rotateButton, scalingWidth, scalingHeight)
  
    /** Kaikilla huonekaluilla on esimerkkikuva. Tehdään siitä imagiView, jotta se voidaan liittää
     * käyttöliittymään ja skaalataan kuva */
    val image = new ImageView(n.image)
    image.fitWidth = 300
    image.fitHeight = 300
  
    /** Lisätään paneeleille oikeat lapset ja lisätään kaikki elementit GridPaneen oikeisiin kohtiin */
    shapePanel.children = Array( shape)
    submitOrDelete.children = Array( submitButton, deleteButton )
    grid.add(label1, 2, 0, 2, 1)
    grid.add(shape, 3, 2)
    grid.add(submitOrDelete, 2, 5,3, 1)
    grid.add(changePanel, 4, 2, 3, 1)
    grid.add( image, 0, 0, 2, 7 )

    /** Mikäli käyttäjä sulkee ikkunan, huonekalua ei lisätä pohjapiirustukseen. */
    stage.onCloseRequest = (event) =>
      var indexOf = listOfFurniture.indexOf(n)
        listOfFurniture.remove(indexOf)
        furnitureIsIn.children.remove( n.shape )
        if n.fname == "Lamp" then
          amountOfLamps -= 1

    val scene = new Scene(grid)
  
    stage.setScene(scene)
  
    stage
    
    
end PopUpMaker
