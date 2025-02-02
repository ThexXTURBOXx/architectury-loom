/*
 * This file is part of fabric-loom, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 FabricMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.fabricmc.loom.task.launch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.architectury.loom.util.ForgeLoggerConfig;
import org.gradle.api.tasks.TaskAction;

import net.fabricmc.loom.task.AbstractLoomTask;

public abstract class GenerateLog4jConfigTask extends AbstractLoomTask {
	@TaskAction
	public void run() {
		Path outputFile = getExtension().getFiles().getDefaultLog4jConfigFile().toPath();

		if (getExtension().isForge() && getExtension().getForge().getUseForgeLoggerConfig().get()) {
			ForgeLoggerConfig.copyToPath(getProject(), outputFile);
			return;
		}

		try (InputStream is = GenerateLog4jConfigTask.class.getClassLoader().getResourceAsStream("log4j2.fabric.xml")) {
			Files.deleteIfExists(outputFile);
			Files.copy(is, outputFile);
		} catch (IOException e) {
			throw new RuntimeException("Failed to generate log4j config", e);
		}
	}
}
