package edu.ncsu.fuzzer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.api.errors.EmtpyCommitException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class HandleGit {
	private Git repository = null;
	private final static String BRANCHHEAD = "refs/heads/fuzzer";
	private final static String BRANCH = "fuzzer";
	private final static String authorName = "FuzzingTool";
	private final static String authorEmail = "fuzzingTool@ncsu.edu";
	private static RevCommit head = null;
	private static RevCommit prevCommit = null;

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
		Ref headRef = repository.getRepository().exactRef(BRANCHHEAD);
		RevWalk walk = new RevWalk(repository.getRepository());
		head = walk.parseCommit(headRef.getObjectId());
		walk.close();
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

	public void addFileToIndex() throws IOException, GitAPIException {
		repository.add().addFilepattern(".").call();
		repository.add().setUpdate(true).addFilepattern(".").call();
	}

	public void commitChanges(String commitMessage) throws GitAPIException, EmtpyCommitException {
		CommitCommand commit = repository.commit().setAuthor(authorName, authorEmail).setAllowEmpty(true);
		prevCommit = commit.setMessage(commitMessage).call();
	}

	public void completeReset() {
		System.out.println("Reset this to " + head);
		ResetCommand command = repository.reset();
		try {
			if (head != null) {
				command.setRef(head.getName());
				command.call();
				repository.reset().setMode(ResetType.HARD).call();
				head = null;
			}
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		System.out.println("Revert this to last " + prevCommit);
		ResetCommand command = repository.reset();
		try {
			if (prevCommit != null) {
				command.setRef(prevCommit.getName());
				command.call();
				repository.reset().setMode(ResetType.HARD).call();
				prevCommit = null;
			}
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void revert() {
		System.out.println("Revert this to " + head);
		RevertCommand command = repository.revert();
		try {
			command.include(head);
			command.call();
			head = null;
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void lapse(long timeInMillis) {
		try {
			Thread.sleep(timeInMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
