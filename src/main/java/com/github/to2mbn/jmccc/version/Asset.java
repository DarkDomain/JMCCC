package com.github.to2mbn.jmccc.version;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import com.github.to2mbn.jmccc.option.MinecraftDirectory;
import com.github.to2mbn.jmccc.util.HexUtils;

public class Asset {

	private String virtualPath;
	private String hash;
	private int size;

	/**
	 * Creates an asset.
	 * 
	 * @param virtualPath the virtual path
	 * @param hash the sha1 hash
	 * @param size the size
	 * @throws NullPointerException if <code>virtualPath==null||hash==null</code>
	 * @throws IllegalArgumentException if <code>size&lt;0</code>
	 */
	public Asset(String virtualPath, String hash, int size) {
		Objects.requireNonNull(virtualPath);
		Objects.requireNonNull(hash);
		if (size < 0) {
			throw new IllegalArgumentException("size<0");
		}

		this.virtualPath = virtualPath;
		this.hash = hash;
		this.size = size;
	}

	/**
	 * Gets the virtual path.
	 * 
	 * @return the virtual path
	 */
	public String getVirtualPath() {
		return virtualPath;
	}

	/**
	 * Gets the sha1 hash of the asset.
	 * 
	 * @return the sha1 hash of the asset
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Gets the size of the asset.
	 * 
	 * @return the size of the asset
	 */
	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return virtualPath + " [hash=" + hash + ", size=" + size + "]";
	}

	/**
	 * Gets the relative path of the asset.
	 * <p>
	 * This method uses '/' as the separator char, and 'assets/objects' as the base dir.<br>
	 * The asset file is located at:
	 * 
	 * <pre>
	 * ${mcdir}/assets/objects/${2-character-prefix of hash}/${hash}
	 * </pre>
	 * 
	 * So the format of the return value is:
	 * 
	 * <pre>
	 * ${2-character-prefix of hash}/${hash}
	 * </pre>
	 * 
	 * @return the relative path of the asset
	 */
	public String getPath() {
		return hash.substring(0, 2) + "/" + hash;
	}

	/**
	 * Validates the asset in the given mcdir.
	 * <p>
	 * This method checks the size, hash of the asset. If, one of them mismatches, or the asset file doesn't exist, this
	 * method will return false. Or this method will return true.
	 * 
	 * @param dir the mcdir where the asset is in
	 * @return true if the asset is valid
	 * @throws IOException if an i/o error occurs
	 * @throws NoSuchAlgorithmException if the default hash algorithm SHA-1 doesn't exist
	 */
	public boolean isValid(MinecraftDirectory dir) throws IOException, NoSuchAlgorithmException {
        File file = new File(dir.getAssetObjects(), getPath());
		if (!file.isFile()) {
			return false;
		}

		if (file.length() != size) {
			return false;
		}

		MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		try (InputStream in = new FileInputStream(file)) {
			byte[] buffer = new byte[8192];
			int read;
			while ((read = in.read(buffer)) != -1) {
				sha1.update(buffer, 0, read);
			}
		}

		return Arrays.equals(sha1.digest(), HexUtils.hexToBytes(hash));
	}

	@Override
	public int hashCode() {
		return Objects.hash(virtualPath, hash, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Asset) {
			Asset another = (Asset) obj;
			return virtualPath.equals(another.virtualPath) && hash.equals(another.hash) && size == another.size;
		}
		return false;
	}

}
