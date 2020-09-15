package com.ilexiconn.llibrary.server.entity;

import net.minecraft.entity.Entity;

/**
 * @param <T> the entity type
 * @author TheCyberBrick
 * @since 1.0.0
 */
public class PropertiesTracker<T extends Entity> {
    private int trackingTimer = 0;
    private int trackingUpdateTimer = 0;
    private boolean trackerReady = false;
    private boolean trackerDataChanged = false;

    private SensitiveTagCompound trackingTag = new SensitiveTagCompound();

    private T entity;
    private EntityProperties<T> properties;

    public PropertiesTracker(T entity, EntityProperties<T> properties) {
        this.entity = entity;
        this.properties = properties;
    }

    /**
     * Updates the tracker
     */
    public void updateTracker() {
        int trackingFrequency = this.properties.getTrackingTime();
        if (trackingFrequency >= 0 && !this.trackerReady) {
            this.trackingTimer++;
            if (this.trackingTimer >= trackingFrequency) {
                this.trackerReady = true;
            }
        }
        int trackingUpdateFrequency = this.properties.getTrackingUpdateTime();
        if (this.trackingUpdateTimer < trackingUpdateFrequency) {
            this.trackingUpdateTimer++;
        }
        if (this.trackingUpdateTimer >= trackingUpdateFrequency) {
            if (!this.trackerDataChanged) {
                this.trackingUpdateTimer = 0;
                if (!this.trackingTag.hasChanged()) this.properties.saveTrackingSensitiveData(trackingTag);
                if (this.trackingTag.hasChanged()) {
                    this.trackerDataChanged = true;
                }
            }
        }
    }

    /**
     * Forces the tracker to sync the next tick
     */
    public void setReady() {
        this.trackerReady = true;
        this.trackerDataChanged = true;
    }

    /**
     * @return true if the data has changed and the tracking timer is ready and resets the tracking timer
     */
    public boolean isTrackerReady() {
        return this.properties.getTrackingTime() >= 0 && this.trackerReady && this.trackerDataChanged;
    }

    /**
     * Called when the data is syncing
     */
    public void onSync() {
        this.properties.onSync();
    }

    /**
     * @return the properties
     */
    public EntityProperties<T> getProperties() {
        return this.properties;
    }

    /**
     * @return the tracked entity
     */
    public T getEntity() {
        return this.entity;
    }

    /**
     * Removes this tracker from the property
     */
    public void removeTracker() {
        this.properties.getTrackers().remove(this);
    }

    /**
     * @return The sensitive tag compound that keeps track of when things change.
     */
	public SensitiveTagCompound getTrackingTag() {
		return trackingTag;
	}

	/**
	 * Resets check states back to defaults (false or 0).
	 */
	public void reset() {
        this.trackingTimer = 0;
        this.trackerReady = false;
        this.trackerDataChanged = false;
        this.trackingTag.reset();
	}
}
