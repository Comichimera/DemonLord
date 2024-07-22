package com.dungeoncrawl.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {
    public MapLoader() {
    }

    public static int[][] loadMap(String path, double[] playerStart) {
        List<int[]> mapList = new ArrayList<>();

        try {
            InputStream is = MapLoader.class.getResourceAsStream(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            for (int row = 0; (line = br.readLine()) != null; row++) {
                String[] tokens = line.split(" ");
                int[] mapRow = new int[tokens.length];

                for (int col = 0; col < tokens.length; col++) {
                    mapRow[col] = Integer.parseInt(tokens[col]);
                    if (mapRow[col] == 3) {
                        playerStart[0] = row + 0.5;
                        playerStart[1] = col + 0.5;
                        mapRow[col] = 0;
                    }
                }
                mapList.add(mapRow);
            }

            br.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapList.toArray(new int[0][]);
    }
}