package edu.ncsu.fuzzer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Ref;

public class HandleGit {
	private Git repository = null;
	private final static String BRANCHHEAD = "refs/heads/fuzzer";
	private final static String BRANCH = "fuzzer";

	public HandleGit(String repositoryURL) throws IOException, GitAPIException {

		Git repository = Git.open(new File(repositoryURL));
		this.repository = repository;
		boolean isPresent = false;
		// List the existing branches
		List<Ref> listRefsBranches = repository.branchList().setListMode(ListMode.ALL).call();

		for (Ref refBranch : listRefsBranches) {
			if (BRANCHHEAD.equals(refBranch.getName())) {
				isPresent = true;
				break;
			}
		}

		if (isPresent)
			checkoutBranch(BRANCH);
		else {
			createBranch(BRANCH);
			checkoutBranch(BRANCH);
		}

	}

	public void setGitRepository(Git repository) {
		this.repository = repository;
	}

	public void createBranch(String branchName) {
		// Create a new branch
		try {
			repository.branchCreate().setName(branchName).call();

		} catch (RefAlreadyExistsException e) {
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidRefNameException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void checkoutBranch(String branchName) {
		// Checkout the new branch
		try {
			repository.checkout().setName(branchName).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void addFileToIndex(File fileToAdd, String gitLocalRepositoryPath) throws IOException, GitAPIException {
		String filePath = fileToAdd.getAbsolutePath().replace("\\", "/").substring(gitLocalRepositoryPath.length() + 1);
		repository.add().addFilepattern(filePath).call();
	}

	public void commitChanges(String commitMessage) throws GitAPIException {
		CommitCommand commit = repository.commit();
		commit.setAllowEmpty(false);
		commit.setMessage(commitMessage).call();
	}
}
