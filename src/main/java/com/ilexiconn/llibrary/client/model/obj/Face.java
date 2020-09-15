package com.ilexiconn.llibrary.client.model.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class Face {
    private Shape parentShape;
    private List<Vertex> vertexList = new ArrayList<>();
    private List<TextureCoords> textureCoordsList = new ArrayList<>();

    public Face(Shape shape) {
        this.parentShape = shape;
    }

    public Face append(Vertex vertex, TextureCoords textureCoords) {
        this.vertexList.add(this.parentShape.addVertex(vertex));
        this.textureCoordsList.add(this.parentShape.addTexCoords(textureCoords));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("f");
        for (int i = 0; i < this.vertexList.size(); i++) {
            sb.append(" ").append(this.vertexList.get(i).getIndex()).append("/").append(this.textureCoordsList.get(i).getIndex());
        }
        return sb.toString();
    }
}
