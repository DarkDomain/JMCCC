package org.to2mbn.jmccc.version;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.to2mbn.jmccc.option.MinecraftDirectory;

public class Version implements Serializable {

	private static final long serialVersionUID = 1L;

	private String version;
	private String type;
	private String mainClass;
	private String assets;
	private String launchArgs;
	private String jarPath;
	private Set<Library> libraries;
	private boolean legacy;
	private AssetIndexDownloadInfo assetIndexDownloadInfo;
	private Map<String, DownloadInfo> downloads;

	/**
	 * Creates a Version object.
	 * 
	 * @param version the version number
	 * @param type the type of the version, null if the type is unknown
	 * @param mainClass the main class
	 * @param assets the assets index name
	 * @param launchArgs the launch arguments
	 * @param jarPath the relative path of the jar file
	 * @param libraries the libraries to add to classpath
	 * @param legacy true if this version is lower than 1.7.10, as well as using the legacy assets index
	 * @param assetIndexDownloadInfo the asset download info, can be null
	 * @param downloads the download infos, can be null
	 * @throws NullPointerException if any of the arguments (except type, assetIndexDownloadInfo, downloads) is null
	 */
	public Version(String version, String type, String mainClass, String assets, String launchArgs, String jarPath, Set<Library> libraries, boolean legacy, AssetIndexDownloadInfo assetIndexDownloadInfo, Map<String, DownloadInfo> downloads) {
		Objects.requireNonNull(version);
		Objects.requireNonNull(mainClass);
		Objects.requireNonNull(assets);
		Objects.requireNonNull(launchArgs);
		Objects.requireNonNull(jarPath);
		Objects.requireNonNull(libraries);
		this.version = version;
		this.type = type;
		this.mainClass = mainClass;
		this.assets = assets;
		this.launchArgs = launchArgs;
		this.jarPath = jarPath;
		this.libraries = libraries;
		this.legacy = legacy;
		this.assetIndexDownloadInfo = assetIndexDownloadInfo;
		this.downloads = downloads;
	}

	/**
	 * Gets the version number.
	 * 
	 * @return the version number
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the type of the version, e.g. "snapshot", "release", null if the type is unknown.
	 * 
	 * @return the type of the version, null if the type is unknown
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the main class.
	 * 
	 * @return the main class
	 */
	public String getMainClass() {
		return mainClass;
	}

	/**
	 * Gets the assets index name.
	 * <p>
	 * Returns <code>"legacy"</code> if it's a legacy version.
	 * 
	 * @return the assets index name
	 * @see #isLegacy()
	 */
	public String getAssets() {
		return assets;
	}

	/**
	 * Gets the launch arguments.
	 * 
	 * @return the launch arguments
	 */
	public String getLaunchArgs() {
		return launchArgs;
	}

	/**
	 * Returns the relative path of the version.
	 * <p>
	 * Use '/' as the separator char, and 'versions' as the base dir.
	 * 
	 * @return the jar file
	 */
	public String getJarPath() {
		return jarPath;
	}

	/**
	 * Gets the required libraries.
	 * 
	 * @return the required libraries
	 */
	public Set<Library> getLibraries() {
		return libraries;
	}

	/**
	 * Returns the missing libraries in the given minecraft directory.
	 * 
	 * @param minecraftDir the minecraft directory to check
	 * @return true the missing libraries in the given minecraft directory, an empty set if no library is missing
	 */
	public Set<Library> getMissingLibraries(MinecraftDirectory minecraftDir) {
		Set<Library> missing = new HashSet<>();
		for (Library library : libraries) {
			if (library.isMissing(minecraftDir)) {
				missing.add(library);
			}
		}
		return missing;
	}

	/**
	 * Returns true if this version is lower than 1.7.10
	 *
	 * @return true if this version is lower than 1.7.10, as well as using the legacy assets index
	 */
	public boolean isLegacy() {
		return legacy;
	}

	/**
	 * Gets the asset download info. This exists only in the version 1.9 or higher.
	 * 
	 * @return the asset download info, can be null
	 */
	public AssetIndexDownloadInfo getAssetIndexDownloadInfo() {
		return assetIndexDownloadInfo;
	}

	/**
	 * Gets the downloads of the version. This exists only in the version 1.9 or higher.
	 * <p>
	 * This maps to the 'downloads' element in the version json.<br>
	 * Here are some known key-value pairs:<br>
	 * <code>client</code> -&gt; the client jar(minecraft client)<br>
	 * <code>server</code> -&gt; the server jar(minecraft server)<br>
	 * 
	 * @return the downloads of the version, can be null
	 */
	public Map<String, DownloadInfo> getDownloads() {
		return downloads;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Version) {
			Version another = (Version) obj;
			return version.equals(another.version) && Objects.equals(type, another.type) && mainClass.equals(another.mainClass) && assets.equals(another.assets) && launchArgs.equals(another.launchArgs) && jarPath.equals(another.jarPath) && libraries.equals(another.libraries) && legacy == another.legacy;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, type, mainClass, assets, launchArgs, jarPath, libraries, legacy);
	}

	@Override
	public String toString() {
		return version;
	}

}