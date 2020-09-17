package com.ilexiconn.llibrary.server.world;

import com.ilexiconn.llibrary.LLibrary;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.ISaveHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.2.0
 */
public enum WorldDataHandler {
    INSTANCE;

    private List<IWorldDataAdapter> dataAdapterList = new ArrayList<>();

    /**
     * Register a world data adapter.
     *
     * @param dataAdapter the adapter instance
     */
    public void registerDataAdapter(IWorldDataAdapter dataAdapter) {
        this.dataAdapterList.add(dataAdapter);
    }

    public void loadWorldData(ISaveHandler saveHandler, World world) {
        if (world.provider.getDimension() == 0) {
            for (IWorldDataAdapter dataAdapter : this.dataAdapterList) {
                File dataFile = new File(this.getSaveFolder(saveHandler, world), dataAdapter.getID() + ".dat");
                if (dataFile.exists()) {
                    try {
                        FileInputStream inputStream = new FileInputStream(dataFile);
                        dataAdapter.loadNBTData(CompressedStreamTools.readCompressed(inputStream), world);
                        inputStream.close();
                    } catch (IOException e) {
                        LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to read file " + dataFile.getName()).getCompleteReport());
                    }
                }
            }
        }
    }

    public void saveWorldData(ISaveHandler saveHandler, World world) {
        if (world.provider.getDimension() == 0) {
            for (IWorldDataAdapter dataAdapter : this.dataAdapterList) {
                File dataFile = new File(this.getSaveFolder(saveHandler, world), dataAdapter.getID() + ".dat");
                if (!dataFile.exists()) {
                    try {
                        dataFile.createNewFile();
                    } catch (IOException e) {
                        LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to create file " + dataFile.getName()).getCompleteReport());
                        continue;
                    }

                    NBTTagCompound compound = new NBTTagCompound();
                    dataAdapter.saveNBTData(compound, world);

                    try {
                        FileOutputStream outputStream = new FileOutputStream(dataFile);
                        CompressedStreamTools.writeCompressed(compound, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to write file " + dataFile.getName()).getCompleteReport());
                    }
                }
            }
        }
    }

    private File getSaveFolder(ISaveHandler saveHandler, World world) {
        File worldFolder;
        IChunkLoader chunkLoader = saveHandler.getChunkLoader(world.provider);
        if (chunkLoader instanceof AnvilChunkLoader) {
            worldFolder = ((AnvilChunkLoader) chunkLoader).chunkSaveLocation;
        } else {
            worldFolder = saveHandler.getWorldDirectory();
        }
        File saveFolder = new File(worldFolder, "com/ilexiconn/llibrary");
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }
        return saveFolder;
    }
}
