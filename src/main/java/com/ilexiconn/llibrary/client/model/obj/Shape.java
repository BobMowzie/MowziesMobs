package com.ilexiconn.llibrary.client.model.obj;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class Shape {
    private String name;
    private OBJModel model;
    private List<Face> faceList = new ArrayList<>();
    private List<Vertex> vertexList = new ArrayList<>();
    private List<TextureCoords> textureCoordsList = new ArrayList<>();

    public Shape(OBJModel parent, String name) {
        this.model = parent;
        this.name = name;
    }

    public Matrix3f rotationMatrix(float angle, float x, float y, float z) {
        angle *= (float) Math.PI / 180.0F;
        Vector3f axis = new Vector3f(x, y, z);
        axis.normalise();
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        float oc = 1.0f - c;

        Matrix3f mat = new Matrix3f();
        mat.m00 = oc * axis.x * axis.x + c;
        mat.m01 = oc * axis.x * axis.y - axis.z * s;
        mat.m02 = oc * axis.z * axis.x + axis.y * s;
        mat.m10 = oc * axis.x * axis.y + axis.z * s;
        mat.m11 = oc * axis.y * axis.y + c;
        mat.m12 = oc * axis.y * axis.z - axis.x * s;
        mat.m20 = oc * axis.z * axis.x - axis.y * s;
        mat.m21 = oc * axis.y * axis.z + axis.x * s;
        mat.m22 = oc * axis.z * axis.z + c;
        return mat;
    }

    public Face addFace(Face face) {
        this.faceList.add(face);
        return face;
    }

    public Vertex addVertex(Vertex vertex) {
        for (Vertex v : this.vertexList) {
            if (v.equals(vertex)) {
                return v;
            }
        }
        this.vertexList.add(vertex);
        vertex.register(this.model);
        return vertex;
    }

    public TextureCoords addTexCoords(TextureCoords textureCoords) {
        for (TextureCoords t : this.textureCoordsList) {
            if (t.equals(textureCoords)) {
                return t;
            }
        }
        this.textureCoordsList.add(textureCoords);
        textureCoords.register(this.model);
        return textureCoords;
    }

    public void translate(Vector3f translationVector) {
        for (Vertex vertex : this.vertexList) {
            Vector3f.add(vertex.getPosition(), translationVector, vertex.getPosition());
        }
    }

    public void scale(Vector3f scaleVector) {
        for (Vertex vertex : this.vertexList) {
            vertex.getPosition().x *= scaleVector.x;
            vertex.getPosition().y *= scaleVector.y;
            vertex.getPosition().z *= scaleVector.z;
        }
    }

    public void rotate(float angle, float x, float y, float z) {
        Matrix3f rotationMatrix = this.rotationMatrix(angle, x, y, z);
        for (Vertex vertex : this.vertexList) {
            Matrix3f.transform(rotationMatrix, vertex.getPosition(), vertex.getPosition());
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("o ").append(this.name).append("\n");
        for (Vertex vertex : this.vertexList) {
            builder.append(vertex.toString()).append("\n");
        }
        for (TextureCoords textureCoords : this.textureCoordsList) {
            builder.append(textureCoords.toString()).append("\n");
        }
        for (Face face : this.faceList) {
            builder.append(face.toString()).append("\n");
        }
        return builder.toString();
    }
}
