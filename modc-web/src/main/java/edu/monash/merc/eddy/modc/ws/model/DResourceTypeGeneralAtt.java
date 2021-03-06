
package edu.monash.merc.eddy.modc.ws.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DResourceTypeGeneralAtt.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DResourceTypeGeneralAtt">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Collection"/>
 *     &lt;enumeration value="Dataset"/>
 *     &lt;enumeration value="Event"/>
 *     &lt;enumeration value="Film"/>
 *     &lt;enumeration value="Image"/>
 *     &lt;enumeration value="InteractiveResource"/>
 *     &lt;enumeration value="Model"/>
 *     &lt;enumeration value="PhysicalObject"/>
 *     &lt;enumeration value="Service"/>
 *     &lt;enumeration value="Software"/>
 *     &lt;enumeration value="Sound"/>
 *     &lt;enumeration value="Text"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DResourceTypeGeneralAtt")
@XmlEnum
public enum DResourceTypeGeneralAtt {

    @XmlEnumValue("Collection")
    COLLECTION("Collection"),
    @XmlEnumValue("Dataset")
    DATASET("Dataset"),
    @XmlEnumValue("Event")
    EVENT("Event"),
    @XmlEnumValue("Film")
    FILM("Film"),
    @XmlEnumValue("Image")
    IMAGE("Image"),
    @XmlEnumValue("InteractiveResource")
    INTERACTIVE_RESOURCE("InteractiveResource"),
    @XmlEnumValue("Model")
    MODEL("Model"),
    @XmlEnumValue("PhysicalObject")
    PHYSICAL_OBJECT("PhysicalObject"),
    @XmlEnumValue("Service")
    SERVICE("Service"),
    @XmlEnumValue("Software")
    SOFTWARE("Software"),
    @XmlEnumValue("Sound")
    SOUND("Sound"),
    @XmlEnumValue("Text")
    TEXT("Text");
    private final String value;

    DResourceTypeGeneralAtt(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DResourceTypeGeneralAtt fromValue(String v) {
        for (DResourceTypeGeneralAtt c: DResourceTypeGeneralAtt.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
