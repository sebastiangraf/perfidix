package org.perfidix.Perclipse.buildpath;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;
import org.perfidix.Perclipse.launcher.PerclipseActivator;

public class BuildPathSupport {
	
	public static class PerfidixPluginDescription{
		private final String bundleID;
		private final VersionRange versionRange;
		private final boolean isOrbitBundle;
		
		public PerfidixPluginDescription(String bundleID, VersionRange versionRange, boolean isOrbitBundle){
			this.bundleID=bundleID;
			this.versionRange=versionRange;
			this.isOrbitBundle=isOrbitBundle;
		}
		
		public Bundle getBundle(){
			Bundle[] bundles = PerclipseActivator.getDefault().getBundles(bundleID, null);
			if(bundles!=null){
				for (Bundle bundle : bundles) {
					Bundle curr=bundle;
					String version = (String) curr.getHeaders().get(Constants.BUNDLE_VERSION);
					if(versionRange.isIncluded(Version.parseVersion(version))){
						return curr;
					}
				}
			}
			
			return null;
		}
		
		public String getBundleID(){
			return bundleID;
		}
		
		public boolean isOrbitBundle(){
			return isOrbitBundle;
		}
		
	}
	
	public static final PerfidixPluginDescription PERFIDIX_PLUGIN= new PerfidixPluginDescription(PerclipseActivator.PERFIDIX_HOME, new VersionRange("[1.0.0,4.4.0)"), false);
	
	public static IPath getBundleLocation(PerfidixPluginDescription pluginDescription){
		Bundle bundle = PerclipseActivator.getDefault().getBundle();
		if(bundle==null){
			return null;
		}
		
		URL local=null;
		try {
			local=FileLocator.toFileURL(bundle.getEntry("/"));
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String fullPath= new File(local.getPath()).getAbsolutePath();
		return Path.fromOSString(fullPath);
	}
	
//	public static IPath getSourceLocation(PerfidixPluginDescription pluginDescription){
//		Bundle bundle=pluginDescription.getBundle();
//		if(bundle==null){
//			return null;
//		}
//		String version =(String) bundle.getHeaders().get(Constants.BUNDLE_VERSION);
//		if(version==null){
//			return null;
//		}
//		Bundle sourceBundle=null;
//		if(pluginDescription.isOrbitBundle()){
//		//I dont know	
//		}
//		else{
//			sourceBundle=PerclipseActivator.getDefault().getBundle("org.perfidix.jdt.source");
//		}
//		if(sourceBundle==null){
//			return null;
//		}
//		URL local=null;
//		try {
//			local=FileLocator.toFileURL(sourceBundle.getEntry("/"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			return null;
//		}
//		String fullPath = new File(local.getPath()).getAbsolutePath() + File.separator+"src"+File.separator+pluginDescription.getBundleID()+"_"+version;
//		return Path.fromOSString(fullPath);
//	}
	
	public static IClasspathEntry getPerfidixClasspathEntry(){
		return JavaCore.newContainerEntry(PerfidixContainerInitializer.PERFIDIX_PATH);
	}
	

	
	public static IClasspathEntry getPerfidixLibraryEntry() {
		IPath bundleBase=getBundleLocation(PERFIDIX_PLUGIN);
		if(bundleBase!=null){
			IPath jarLocation=bundleBase.append("/lib/perfidix-3.3-jar-with-dependencies.jar");
//			IPath sourceBase= getSourceLocation(PERFIDIX_PLUGIN);
//			IPath srcLocation= sourceBase!=null ? sourceBase.append("perfidixsrc.zip") : null;
			IAccessRule[] accessRule={ };
			String javadocLocation= PerclipseActivator.getDefault().getPreferenceStore().getString("This is the Perfidix JavaDoc");
			IClasspathAttribute[] attributes={
					JavaCore.newClasspathAttribute(IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, javadocLocation)
			};
			return JavaCore.newLibraryEntry(jarLocation, null, null, accessRule, attributes, false);
			
		}
		return null;
		
	}

}
