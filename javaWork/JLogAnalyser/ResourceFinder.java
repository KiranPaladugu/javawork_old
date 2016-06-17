import java.net.URL;

public class ResourceFinder extends ClassLoader {
	private static ResourceFinder finder = new ResourceFinder();

	private ResourceFinder() {
	}

	public static ResourceFinder getResourceFinder() {
		return finder;
	}

	public URL findResource(String name) {
		return super.findResource(name);
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

	@Override
	public String findLibrary(String libname) {
		return super.findLibrary(libname);
	}

	@Override
	public Package getPackage(String name) {
		return super.getPackage(name);
	}

	@Override
	public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		return super.loadClass(name, resolve);
	}
}
