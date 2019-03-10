package edu.ncsu.fuzzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.api.errors.GitAPIException;

public class Application {

	public static void main(String[] args) {
		System.out.println("Hello fuzzer");
		try {
			Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
			String repoURL = currentRelativePath.toString() + "/iTrust2-v4";
			// System.out.println("Current relative path is: " + repoURL);
			HandleGit git = new HandleGit(repoURL);
			ItrustFuzzing fuzzing = new ItrustFuzzing(repoURL, git);
			//git.addFileToIndex();
			//git.commitChanges("new test commit 3");
			//git.revert();
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	}

}