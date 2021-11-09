package model;

/**
 * EObjectCategory implements categories that apply to the engineering objects in the organisation (eg. boat, car).
 *
 * The categories are used to differentiate engineering objects
 * and display relevant reusable content for these - in the form of ContentBlocks.
 *
 */

public class EObjectCategory {

    // ----- Properties -----
    private Integer eObjectCatId;
    private String name;

    // ----- Constructors -----

    /**
     * Empty constructor
     */
    public EObjectCategory() {
    }


    // ----- Getters and setters -----

    public Integer geteObjectCatId() {
        return eObjectCatId;
    }

    public void seteObjectCatId(Integer eObjectCatId) {
        this.eObjectCatId = eObjectCatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}