/*
 * SonarQube Findbugs Plugin
 * Copyright (C) 2012 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.findbugs.resource;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Load <code>SourceDebugExtension</code> section from class file.
 *
 * <code>SourceDebugExtension</code> is use to store optionally the line of code mapping.
 * The code content if not empty is must likely format according to JSR45 (SMAP).
 */
public class DebugExtensionExtractor {

    @Nullable
    public String getDebugExtFromClass(InputStream classIn) throws IOException {

        AbstractClassVisitor visitor = new AbstractClassVisitor();
        try {
            ClassReader classReader = new ClassReader(classIn);
            classReader.accept(visitor, 0);

            return visitor.debug;
        }
        catch (Exception e) {
            throw new ClassMetadataLoadingException(e);
        }
    }

    public String getDebugSourceFromClass(InputStream classIn) throws IOException {

        AbstractClassVisitor visitor = new AbstractClassVisitor();
        try {
            ClassReader classReader= new ClassReader(classIn);
            classReader.accept(visitor, 0);

            return visitor.source;
        }
        catch (Exception e) {
            throw new ClassMetadataLoadingException(e);
        }
    }

    private static class AbstractClassVisitor extends ClassVisitor {

        protected String source;
        protected String debug;

        public AbstractClassVisitor() {
            super(Opcodes.ASM7);
        }

        @Override
        public void visitSource(String source, String debug) {
            super.visitSource(source, debug);
            this.source = source;
            this.debug  = debug;
        }
    }
}
