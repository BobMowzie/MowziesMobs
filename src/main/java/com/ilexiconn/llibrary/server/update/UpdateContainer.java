package com.ilexiconn.llibrary.server.update;

import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * A container used by the update checker. This instance get's generated when an update checker gets registered.
 *
 * @author iLexiconn
 * @since 1.0.0
 */
public class UpdateContainer {
    private transient ModContainer modContainer;
    private transient BufferedImage icon;
    private transient ArtifactVersion latestVersion;
    private String version;
    private String updateURL;
    private String iconURL;
    private Map<String, String[]> versions;

    /**
     * @return the mod container of this update checker
     */
    public ModContainer getModContainer() {
        return this.modContainer;
    }

    /**
     * Set the mod container update container.
     *
     * @param modContainer the new mod container
     */
    public void setModContainer(ModContainer modContainer) {
        this.modContainer = modContainer;
    }

    /**
     * @return the icon of this update container
     */
    public BufferedImage getIcon() {
        return this.icon;
    }

    /**
     * Set the icon of this update container.
     *
     * @param icon the new icon
     */
    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    /**
     * @return the latest version
     */
    public ArtifactVersion getLatestVersion() {
        if (this.latestVersion == null) {
            this.latestVersion = new DefaultArtifactVersion(this.version);
        }
        return this.latestVersion;
    }

    /**
     * @return the latest version as String
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * @return the update url
     */
    public String getUpdateURL() {
        return this.updateURL;
    }

    /**
     * @return the icon url
     */
    public String getIconURL() {
        return this.iconURL;
    }

    /**
     * @return a map with all versions and changelogs
     */
    public Map<String, String[]> getVersions() {
        return this.versions;
    }
}
