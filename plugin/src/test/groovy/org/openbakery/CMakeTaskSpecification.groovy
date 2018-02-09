package org.openbakery

import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class CMakeTaskSpecification extends Specification {


	CMakeXCodeGenerator xcodeGenerateCMakeTask
	Project project

	def setup() {

		File projectDir = new File("../example/iOS/cmakeExample/XCode")
		project = ProjectBuilder.builder().withProjectDir(projectDir).build()
		project.buildDir = new File(System.getProperty("java.io.tmpdir"), "gradle-xcodebuild")
		project.buildDir.mkdirs()
		project.apply plugin: XcodePlugin
		project.cmakeXCode.cmakeBinary = "/usr/local/bin/cmake"
		project.cmakeXCode.cmakeFile = "CMakeLists.txt"
		project.cmakeXCode.arguments = "-DMyLongVerStr=longVerStr-0.0 -DMyShortVerStr=0.0"
		project.cmakeXCode.buildDir = "XCodeProj"

		xcodeGenerateCMakeTask = project.getTasks().getByPath(XcodePlugin.CMAKE_XCODE_GENERATOR)
	}

	def cleanup() {
		FileUtils.deleteDirectory(project.buildDir)
	}

	def "test GenerateCMakeXCodeTask"() {
		when:
		xcodeGenerateCMakeTask.build()
		File appFile = new File(project.projectDir, "XCodeProj")


		then:
		appFile.exists()
	}


}
