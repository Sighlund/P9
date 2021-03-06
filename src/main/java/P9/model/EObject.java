package P9.model;

import P9.persistence.TextBlockDao;

import javax.persistence.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * EObject implements an 'engineering object' - the name is derived from Bluestar PLM.
 * Engineering objects are the objects that engineers work with in the Bluestar PLM solution.
 *
 * A single EObject can either represent a product that the organisation sells
 * or a component that is part of another engineering object.
 *
 * An EObject holds information we assume is available in the Bluestar PLM solution.
 *
 * The class is mapped with Hibernate JPA. See: https://www.baeldung.com/jpa-entities
 * JPA many to many mapping, see https://www.baeldung.com/hibernate-many-to-many
 *
 * The class is mapped with XML.bind annotations.
 * See: https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/annotation/package-summary.html
 */


@XmlRootElement
@Entity
@Table(name = "e_object")
public class EObject {

    // ----- Properties ----
    @Id
    @Column(name = "e_object_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generates an unique value for every identity
    private Integer eObjectId;
    private String name;
    private Double version;
    private Double length;
    private Double height;
    private Double width;
    private Double weight;
    @Column(name ="image_path")
    private String imagePath;

    // Maps a many-to-many relation between eObject and other eObjects (components), cascading all actions
    // An eObject has a list of all its "first-layer" components
    @ManyToMany(fetch = FetchType.EAGER)
    // The association uses the join/link table "e_object_has_e_object"
    @JoinTable(name = "e_object_has_e_object",
            // The two columns are foreign keys to id columns in the user table and the eObject table
            // The parent e_object is the "owning" part of the association. The component e_object is the inverse part.
            joinColumns = { @JoinColumn(name = "e_object_id")},
            inverseJoinColumns = { @JoinColumn(name = "e_object_id_child")}
    )
    private List<EObject> componentList = new ArrayList<>();

    // Maps a many-to-one relation between eObject and category, cascading all actions
    @ManyToOne(fetch = FetchType.EAGER)
    // The association uses the join column "e_object_category_id" in the e_object table
    // which references the id column in the category table
    @JoinColumn(name = "e_object_category_id", referencedColumnName = "e_object_category_id")
    private EObjectCategory category;

    // Maps a one-to-one relation between eObject and eObjectDoc
    // The association is mapped by the field "eObject" in EObjectDoc.java
    @OneToOne(mappedBy = "eObject")
    private EObjectDoc doc;

    // ----- Constructor -----
    /**
     * Empty constructor
     */
    public EObject() {
    }

    // ----- Getters and setters -----
    public Integer geteObjectId() {
        return eObjectId;
    }

    @XmlAttribute
    public void seteObjectId(Integer eObjectId) {
        this.eObjectId = eObjectId;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public Double getVersion() {
        return version;
    }

    @XmlElement
    public void setVersion(Double version) {
        this.version = version;
    }

    public Double getLength() {
        return length;
    }

    @XmlElement
    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeight() {
        return height;
    }

    @XmlElement
    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    @XmlElement
    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getWeight() {
        return weight;
    }

    @XmlElement
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getImagePath() {
        return imagePath;
    }

    @XmlElement
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @XmlTransient
    public List<EObject> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<EObject> componentList) {
        this.componentList = componentList;
    }

    @XmlTransient
    public EObjectCategory getCategory() {
        return category;
    }

    public void setCategory(EObjectCategory category) {
        this.category = category;
    }

    @XmlTransient
    public EObjectDoc getDoc() {
        return doc;
    }

    public void setDoc(EObjectDoc doc) {
        this.doc = doc;
    }

    // ----- Instance methods -----
    /**
     * Method that creates a new Doc object and associates it with the eObject instance
     */
    public void createNewDoc(){
        // Create new doc object, and set doc property to reference it
        doc = new EObjectDoc();

        // Make doc reference this eObject instance
        doc.seteObject(this);

        // Set doc to equal template from database
        TextBlockDao txtDao = new TextBlockDao();
        TextBlock txt = txtDao.getById(4);
        doc.setText(txt.getTxt());
    }

    /**
     * Method to convert eObject to xml file
     * which is stored in repository ressources/xml
     */
    public void eObjectToXML(){
        //Passes EObject attribute values to create XML file
        try
        {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(EObject.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Formats file and bind it to xsl
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                    "<?xml-stylesheet type='text/xsl' href='style.xsl' ?>");

            //Store XML to File
            File file = new File("src/main/resources/xml/eObject.xml");

            //Writes XML file to file-system
            jaxbMarshaller.marshal(this, file);
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the names of an eObjects components as a String
     * @return String concatenated from the names of the eObject's components
     */
    public String componentNames(){
        String componentNames = "";

        // Add name for every component to the concatenated string
        for (EObject component : componentList){
            componentNames += component.getName() + " ";
        }

        return componentNames;
    }
}
