package dev.architectury.loom.metadata;

import com.electronwill.nightconfig.core.io.ParsingException;

import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.stream.Collectors;

import net.fabricmc.loom.configuration.ifaceinject.InterfaceInjectionProcessor;
import net.fabricmc.loom.util.ExceptionUtil;
import net.fabricmc.loom.util.ModPlatform;

import org.jetbrains.annotations.Nullable;

public final class McmodInfo implements ModMetadataFile {
	public static final String FILE_PATH = "mcmod.info";
	private final List<Map<String, String>> config;

	private McmodInfo(List<Map<String, String>> config) {
		this.config = config;
	}

	public static McmodInfo of(byte[] utf8) {
		return of(new String(utf8, StandardCharsets.UTF_8));
	}

	public static McmodInfo of(String text) {
		try {
			return new McmodInfo(new GsonBuilder().create().fromJson(text, new TypeToken<List<Map<String, String>>>() {
			}.getType()));
		} catch (ParsingException e) {
			throw ExceptionUtil.createDescriptiveWrapper(IllegalArgumentException::new, "Could not parse mods.toml", e);
		}
	}

	public static McmodInfo of(Path path) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			return new McmodInfo(new GsonBuilder().create().fromJson(reader, new TypeToken<List<Map<String, String>>>() {
			}.getType()));
		} catch (ParsingException e) {
			throw ExceptionUtil.createDescriptiveWrapper(IllegalArgumentException::new, "Could not parse mods.toml", e);
		}
	}

	public static McmodInfo of(File file) throws IOException {
		return of(file.toPath());
	}

	@Override
	public Set<String> getIds() {
		return config.stream().map(s -> s.get("modid")).collect(Collectors.toSet());
	}

	@Override
	public Set<String> getAccessWideners() {
		return Set.of();
	}

	@Override
	public Set<String> getAccessTransformers(ModPlatform platform) {
		// TODO: Needs to access MANIFEST
		return Set.of();
	}

	@Override
	public List<InterfaceInjectionProcessor.InjectedInterface> getInjectedInterfaces(@Nullable String modId) {
		return List.of();
	}

	@Override
	public String getFileName() {
		return FILE_PATH;
	}

	@Override
	public List<String> getMixinConfigs() {
		return List.of();
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof McmodInfo modsToml && modsToml.config.equals(config);
	}

	@Override
	public int hashCode() {
		return config.hashCode();
	}
}
