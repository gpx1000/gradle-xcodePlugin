package org.openbakery.internal

import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.openbakery.Devices
import org.openbakery.XcodePlugin
import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

/**
 * Created by rene on 14.08.15.
 */
class XcodeBuildSpecTest {

	XcodeBuildSpec buildSpec
	XcodeBuildSpec parentBuildSpec
	Project project

	@Before
	void setup() {
		File projectDir =  new File(System.getProperty("java.io.tmpdir"), "gradle-xcodebuild")
		project = ProjectBuilder.builder().withProjectDir(projectDir).build()
		project.apply plugin: org.openbakery.XcodePlugin


		parentBuildSpec = new XcodeBuildSpec(project);
		buildSpec = new XcodeBuildSpec(project, parentBuildSpec);

	}

	@After
	void cleanup() {
		File projectDir =  new File(System.getProperty("java.io.tmpdir"), "gradle-xcodebuild")
		FileUtils.deleteDirectory(projectDir)
	}


	@Test
	void testDefaultValues() {
		assertThat(buildSpec.sdk, is(equalTo(XcodePlugin.SDK_IPHONESIMULATOR)))
		assertThat(buildSpec.configuration, is(equalTo('Debug')))
		assertThat(buildSpec.devices, is(equalTo(Devices.UNIVERSAL)))
	}

	@Test
	void testMergeNothing() {
		buildSpec.target = "Test"
		parentBuildSpec.target = "Build"

		assertThat(buildSpec.target, is(equalTo("Test")))
	}


	@Test
	void testMerge() {
		buildSpec.target = ""
		parentBuildSpec.target = "Build"

		assertThat(buildSpec.target, is(equalTo("Build")))
	}

	@Test
	void testMergeEmpty() {
		buildSpec.target = null
		parentBuildSpec.target = ""

		assertThat(buildSpec.target, is(nullValue()))
	}

	@Test
	void testMergeConfiguration() {
		buildSpec.target = "Test"
		parentBuildSpec.configuration = "Debug"
		parentBuildSpec.sdk = "macosx"

		assertThat(buildSpec.target, is(equalTo("Test")))
		assertThat(buildSpec.configuration, is(equalTo("Debug")))
		assertThat(buildSpec.sdk, is(equalTo("macosx")))
	}


	@Test
	void testMergeVersion() {
		parentBuildSpec.version = "version"
		assertThat(buildSpec.version, is(equalTo("version")));
	}


	@Test
	void testMergeScheme() {
		parentBuildSpec.scheme = "scheme"
		assertThat(buildSpec.scheme, is(equalTo("scheme")));
	}


	@Test
	void testConfiguration() {
		parentBuildSpec.configuration = "configuration"
		assertThat(buildSpec.configuration, is(equalTo("configuration")));
	}

	@Test
	void testSdk() {
		parentBuildSpec.sdk = "sdk"
		assertThat(buildSpec.sdk, is(equalTo("sdk")));
	}

	@Test
	void testIpaFileName() {
		parentBuildSpec.ipaFileName = "ipaFileName"
		assertThat(buildSpec.ipaFileName, is(equalTo("ipaFileName")));
	}

	@Test
	void testWorkspace() {
		parentBuildSpec.workspace = "workspace"
		assertThat(buildSpec.workspace, is(equalTo("workspace")));
	}

	@Test
	void testDevices() {
		parentBuildSpec.devices = Devices.PAD
		assertThat(buildSpec.devices, is(Devices.PAD));
	}

	@Test
	void testProductName() {
		parentBuildSpec.productName = "productName"
		assertThat(buildSpec.productName, is(equalTo("productName")));
	}

	@Test
	void testInfoPlist() {
		parentBuildSpec.infoPlist = "infoPlist"
		assertThat(buildSpec.infoPlist, is(equalTo("infoPlist")));
	}

	@Test
	void testProductType() {
		parentBuildSpec.productType = "productType"
		assertThat(buildSpec.productType, is(equalTo("productType")));
	}

	@Test
	void testBundleName() {
		parentBuildSpec.bundleName = "bundleName"
		assertThat(buildSpec.bundleName, is(equalTo("bundleName")));
	}


	@Test
	void testSymRoot() {
		parentBuildSpec.symRoot = "symRoot"
		assertThat(buildSpec.symRoot, is(instanceOf(File)));
		assertThat(buildSpec.symRoot.absolutePath, endsWith("symRoot"));
	}

	@Test
	void testDstRoot() {
		parentBuildSpec.dstRoot = "dstRoot"
		assertThat(buildSpec.dstRoot, is(instanceOf(File)));
		assertThat(buildSpec.dstRoot.absolutePath, endsWith("dstRoot"));
	}

	@Test
	void testObjRoot() {
		parentBuildSpec.objRoot = "objRoot"
		assertThat(buildSpec.objRoot, is(instanceOf(File)));
		assertThat(buildSpec.objRoot.absolutePath, endsWith("objRoot"));
	}

	@Test
	void testSharedPrecompsDir() {
		parentBuildSpec.sharedPrecompsDir = "sharedPrecompsDir"
		assertThat(buildSpec.sharedPrecompsDir, is(instanceOf(File)));
		assertThat(buildSpec.sharedPrecompsDir.absolutePath, endsWith("sharedPrecompsDir"))
	}


	@Test
	void testBundleNameSuffix() {
		parentBuildSpec.bundleNameSuffix = "bundleNameSuffix"
		assertThat(buildSpec.bundleNameSuffix, is("bundleNameSuffix"));
	}


	@Test
	void testWorkspaceNil() {
		assert buildSpec.workspace == null;
	}

	@Test
	void testWorkspaceValue() {

		File workspace = new File(project.projectDir , "Test.xcworkspace")
		workspace.mkdirs()
		assert buildSpec.workspace == "Test.xcworkspace";

	}


	@Test
	void init() {

		buildSpec.sdk = "macosx"
		assertThat(buildSpec.sdk, is("macosx"))

		buildSpec.sdk = "iphoneos"
		assertThat(buildSpec.sdk, is("iphoneos"))


	}


}