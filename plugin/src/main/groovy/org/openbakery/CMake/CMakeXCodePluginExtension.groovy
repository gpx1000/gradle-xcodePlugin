package org.openbakery.CMake

import org.gradle.api.Project

class CMakeXCodePluginExtension {

    def String cmakeBinary=null
    def String cmakeFile=null
    def String arguments=null
    def String buildDir=null
    private final Project project

    public CMakeXCodePluginExtension(Project project) {
        this.project = project
    }
}
