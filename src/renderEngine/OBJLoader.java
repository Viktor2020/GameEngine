package renderEngine;


import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {
    private static String SPLIT = " ";
    private static String SPLIT_TRIANGLE = "/";

    public static RawModel loadObjModel(String fileName, Loader loader) {
        FileReader fr = null;

        try {
            fr = new FileReader(new File("res/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file :" + fileName);
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(fr);
        String line = "";
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] texturesArray = null;
        int[] indicesArray = null;

        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(SPLIT);

                if (line.startsWith("v ")) { // vertex
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) { // texture
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) { // normal
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) { // face
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(SPLIT);
                String[] vertex1 = currentLine[1].split(SPLIT_TRIANGLE);
                String[] vertex2 = currentLine[2].split(SPLIT_TRIANGLE);
                String[] vertex3 = currentLine[3].split(SPLIT_TRIANGLE);

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            System.err.println("Error parse line :" + line);
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
                                      List<Vector3f> normals, float[] texturesArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        texturesArray[currentVertexPointer * 2] = currentTex.x;
        texturesArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }
}
