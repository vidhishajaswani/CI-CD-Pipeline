package edu.ncsu.fuzzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.javaparser.ParseProblemException;

public class Application {
	static HandleGit git = null;

	public static void main(String[] args) {
		System.out.println("Fuzzing Started!");
		try {
			Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
			String repoURL = currentRelativePath.toString() + "/iTrust2-v4";
			// System.out.println("Current relative path is: " + repoURL);
			git = new HandleGit(repoURL);
			ItrustFuzzing fuzzing = new ItrustFuzzing(repoURL, git);
			fuzzing.doFuzzing();
		} catch (IOException | GitAPIException | ParseProblemException e) {
			e.printStackTrace();
		} finally {
			git.lapse(10000);
			git.reset(); // reset back to original commit
		}
	}

}