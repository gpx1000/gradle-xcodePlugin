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

import org.gradle.api.tasks.TaskAction
import org.openbakery.xcode.Xcodebuild

import static groovy.io.FileType.ANY
import static groovy.io.FileVisitResult.SKIP_SUBTREE

class XcodeBuildTask extends AbstractXcodeBuildTask {

	XcodeBuildTask() {
		super()

		dependsOn(
				XcodePlugin.CMAKE_XCODE_GENERATOR,
				XcodePlugin.XCODE_CONFIG_TASK_NAME,
				XcodePlugin.INFOPLIST_MODIFY_TASK_NAME
		)
		this.description = "Builds the Xcode project"
	}

	@TaskAction
	def build() {

		parameters = project.xcodebuild.xcodebuildParameters.merge(parameters)

		if (parameters.scheme == null && parameters.target == null) {
			throw new IllegalArgumentException("No 'scheme' or 'target' specified, so do not know what to build");
		}
		final excludedDirs = ['.svn', '.git', '.hg', '.idea', 'node_modules', '.gradle', 'CMakeFiles']
		def file = null
		project.projectDir.traverse(
				type         : ANY,
				nameFilter	 : ~/.*\.xcodeproj$/,
				preDir       : { if (it.name in excludedDirs) return SKIP_SUBTREE },
				visitRoot	 : true) {
			if(file == null) {
				file = it.parentFile
			}
		}

		if (!project.getBuildDir().exists()) {
			project.getBuildDir().mkdirs()
		}

		File outputFile = new File(project.getBuildDir(), "xcodebuild-output.txt")
		commandRunner.setOutputFile(outputFile)

		logger.debug("using xcode {}", xcode)

		Xcodebuild xcodebuild
		if(file == null)
			xcodebuild = new Xcodebuild(project.projectDir, commandRunner, xcode, parameters, getDestinations())
		else
			xcodebuild = new Xcodebuild(file, commandRunner, xcode, parameters, getDestinations())

		xcodebuild.execute(createXcodeBuildOutputAppender("XcodeBuildTask") , project.xcodebuild.environment)
		logger.lifecycle("Done")
	}



}
