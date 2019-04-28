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
			int commits = parseCommits(args);
			Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
			String repoURL = currentRelativePath.toString() + "/iTrust2-v4";
			// System.out.println("Current relative path is: " + repoURL);
			git = new HandleGit(repoURL);
			ItrustFuzzing fuzzing = new ItrustFuzzing(repoURL, git, commits);
			fuzzing.doFuzzing();
		} catch (IOException | GitAPIException | ParseProblemException e) {
			e.printStackTrace();
		} finally {
			git.lapse(45000);
			git.reset(); // reset back to original commit
			System.out.println("Finally: Resetting to master");
			git.checkoutBranch("master");
		}
	}

	private static int parseCommits(String[] args) {
		try {
			return Integer.parseInt(args[0]);
		} catch (Exception e) {
			// Don' t wish to print any msg!
			// Code should not fail because of this
		}
		return -1; // default value, can't be parsed!
	}

}