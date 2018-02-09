/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openbakery

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class CMakeXCodeGenerator extends AbstractXcodeTask {

	private File CMakeListsFile
	public File OutputDir
	private String Arguments
	private String CMakeBin
	private static String stdArgs = "-DCMAKE_TOOLCHAIN_FILE=CMake/ios.cmake " +
			" -DCMAKE_MACOSX_BUNDLE=ON -DCMAKE_XCODE_ATTRIBUTE_CODE_SIGNING_REQUIRED=ON "
	private static String stdArgsEnd = " -DIOS_PLATFORM=OS "
	private static String noArgsDef =  " -DMyLongVerStr=longVerStr-0.0 -DMyShortVerStr=0.0 "
	private static String GeneratorArgs = "-H. -BXcode -GXcode"

	CMakeXCodeGenerator() {
		super()
		this.description = "generates the XCode Project"
	}

	@TaskAction
	def build() {
		if(project.cmakeXCode.cmakeBinary != null)
			CMakeBin = project.cmakeXCode.cmakeBinary
		else
			CMakeBin = "cmake"
		if(project.cmakeXCode.cmakeFile != null)
			CMakeListsFile = new File(project.projectDir, project.cmakeXCode.cmakeFile)
		else
			CMakeListsFile = null
		if(project.cmakeXCode.buildDir != null)
			OutputDir = new File(project.projectDir, project.cmakeXCode.buildDir)
		else
			OutputDir = new File(project.projectDir,"cmakeBuild")
		OutputDir.mkdirs()
		logger.lifecycle(OutputDir.absolutePath)
		Arguments = stdArgs
		if(project.cmakeXCode.arguments != null)
			Arguments += project.cmakeXCode.arguments
		else
			Arguments += noArgsDef
		Arguments += stdArgsEnd


		if(CMakeListsFile == null)
			return

		def commandList = [CMakeBin]

		commandList.addAll(Arguments.split())
		commandList.addAll(GeneratorArgs.split())
		commandList.add(CMakeListsFile.parentFile.absolutePath)

		File outputFile = new File(OutputDir.absolutePath, "xcodebuild-output.txt")
		commandRunner.setOutputFile(outputFile)
		commandRunner.runWithResult(OutputDir.absolutePath, commandList)

		logger.lifecycle("Done")
	}
}
