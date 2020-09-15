package com.ilexiconn.llibrary.client.model.techne;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.ilexiconn.llibrary.LLibrary;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class TechneModel {
    private List<TechneCube> cubes = new ArrayList<>();
    private float scaleX = 1.0F;
    private float scaleY = 1.0F;
    private float scaleZ = 1.0F;
    private int textureWidth = 64;
    private int textureHeight = 32;
    private String fileName;

    private TechneModel(File file) {
        try {
            this.fileName = file.getName();
            ZipFile zipFile = new ZipFile(file);
            InputStream stream = null;
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            boolean xml = false;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.getName().startsWith("model.")) {
                    continue;
                }
                xml = entry.getName().endsWith("xml");
                stream = zipFile.getInputStream(entry);
                break;
            }
            if (stream == null) {
                return;
            }
            byte[] modelXml = IOUtils.toByteArray(stream);
            zipFile.close();
            ByteArrayInputStream is = new ByteArrayInputStream(modelXml);
            if (xml) {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(is);
                NodeList nodeListModel = document.getElementsByTagName("Model");
                NodeList nodes = nodeListModel.item(0).getChildNodes();
                int i = 0;
                while (i < nodes.getLength()) {
                    Node node = nodes.item(i);
                    if (node.getNodeName().equals("GlScale")) {
                        String[] scale = node.getTextContent().split(",");
                        this.scaleX = Float.parseFloat(scale[0]);
                        this.scaleY = Float.parseFloat(scale[1]);
                        this.scaleZ = Float.parseFloat(scale[2]);
                    }
                    if (node.getNodeName().equals("TextureSize")) {
                        String[] textureSize = node.getTextContent().split(",");
                        this.textureWidth = Integer.parseInt(textureSize[0]);
                        this.textureHeight = Integer.parseInt(textureSize[1]);
                    }
                    ++i;
                }
                NodeList shapes = document.getElementsByTagName("Shape");
                int shapeIndex = 0;
                while (shapeIndex < shapes.getLength()) {
                    Node shape = shapes.item(shapeIndex);
                    NamedNodeMap shapeAttributes = shape.getAttributes();
                    if (shapeAttributes == null) {
                        throw new RuntimeException("Cube " + (shapeIndex + 1) + " has no attributes");
                    }
                    Node name = shapeAttributes.getNamedItem("name");
                    String shapeName = null;
                    if (name != null) {
                        shapeName = name.getNodeValue();
                    }
                    if (shapeName == null) {
                        shapeName = "cube " + (shapeIndex + 1);
                    }
                    try {
                        boolean mirrored = false;
                        String[] offset = new String[3];
                        String[] position = new String[3];
                        String[] rotation = new String[3];
                        String[] size = new String[3];
                        String[] textureOffset = new String[2];
                        NodeList shapeChildren = shape.getChildNodes();
                        int childIndex = 0;
                        while (childIndex < shapeChildren.getLength()) {
                            Node shapeChild = shapeChildren.item(childIndex);
                            String shapeChildName = shapeChild.getNodeName();
                            String shapeChildValue = shapeChild.getTextContent();
                            if (shapeChildValue != null) {
                                shapeChildValue = shapeChildValue.trim();
                                switch (shapeChildName) {
                                    case "IsMirrored":
                                        mirrored = !shapeChildValue.equals("False");
                                        break;
                                    case "Offset":
                                        offset = shapeChildValue.split(",");
                                        break;
                                    case "Position":
                                        position = shapeChildValue.split(",");
                                        break;
                                    case "Rotation":
                                        rotation = shapeChildValue.split(",");
                                        break;
                                    case "Size":
                                        size = shapeChildValue.split(",");
                                        break;
                                    case "TextureOffset":
                                        textureOffset = shapeChildValue.split(",");
                                        break;
                                }
                            }
                            ++childIndex;
                        }
                        TechneCube cube = TechneCube.create(shapeName);
                        cube.setTexture(Integer.parseInt(textureOffset[0]), Integer.parseInt(textureOffset[1]));
                        cube.setTextureMirrored(mirrored);
                        cube.setOffset(Float.parseFloat(offset[0]), Float.parseFloat(offset[1]), Float.parseFloat(offset[2]));
                        cube.setDimensions(Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]));
                        cube.setPosition(Float.parseFloat(position[0]), Float.parseFloat(position[1]) - 23.4f, Float.parseFloat(position[2]));
                        cube.setRotation(Float.parseFloat(rotation[0]), Float.parseFloat(rotation[1]), Float.parseFloat(rotation[2]));
                        this.cubes.add(cube);
                    } catch (NumberFormatException e) {
                        LLibrary.LOGGER.error("Failed to parse techne cuboid", e);
                    }
                    ++shapeIndex;
                }
            } else {
                JsonParser jsonParser = new JsonParser();
                JsonReader reader = new JsonReader(new InputStreamReader(is));
                reader.setLenient(true);
                JsonElement rootElement = jsonParser.parse(reader);
                JsonObject techne = rootElement.getAsJsonObject().getAsJsonObject("Techne");
                JsonArray models = techne.getAsJsonArray("Models");
                for (int i = 0; i < models.size(); i++) {
                    JsonElement modelEntryElement = models.get(i);
                    if (modelEntryElement.isJsonObject()) {
                        JsonObject modelEntryObject = modelEntryElement.getAsJsonObject();
                        JsonObject modelObject = modelEntryObject.getAsJsonObject("Model");
                        JsonObject geometry = modelObject.getAsJsonObject("Geometry");
                        JsonArray shapes = geometry.getAsJsonArray("Shape");
                        for (int shape = 0; shape < shapes.size(); shape++) {
                            JsonElement shapeElement = shapes.get(shape);
                            if (shapeElement.isJsonObject()) {
                                JsonObject shapeObject = shapeElement.getAsJsonObject();
                                TechneCube cube = TechneCube.create(shapeObject.get("@name").getAsString());
                                cube.setTextureMirrored(shapeObject.get("IsMirrored").getAsBoolean());
                                String[] offsets = shapeObject.get("Offset").getAsString().split(",");
                                cube.setOffset(Float.parseFloat(offsets[0]), Float.parseFloat(offsets[1]), Float.parseFloat(offsets[2]));
                                String[] positions = shapeObject.get("Position").getAsString().split(",");
                                cube.setPosition(Float.parseFloat(positions[0]), Float.parseFloat(positions[1]), Float.parseFloat(positions[2]));
                                String[] rotations = shapeObject.get("Rotation").getAsString().split(",");
                                cube.setRotation(Float.parseFloat(rotations[0]), Float.parseFloat(rotations[1]), Float.parseFloat(rotations[2]));
                                String[] sizes = shapeObject.get("Size").getAsString().split(",");
                                cube.setDimensions(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]), Integer.parseInt(sizes[2]));
                                String[] textureOffset = shapeObject.get("TextureOffset").getAsString().split(",");
                                cube.setTexture(Integer.parseInt(textureOffset[0]), Integer.parseInt(textureOffset[1]));
                                this.cubes.add(cube);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TechneModel fromFile(File file) {
        return new TechneModel(file);
    }

    public List<TechneCube> getCubes() {
        return this.cubes;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public float getScaleZ() {
        return this.scaleZ;
    }

    public int getTextureWidth() {
        return this.textureWidth;
    }

    public int getTextureHeight() {
        return this.textureHeight;
    }

    public String getFileName() {
        return this.fileName;
    }
}
