# Git Workflow

## The Main Branch

This workflow uses one main branch, `master`. This branch is the main, eternal branch and will always represent the latest, stable version of the code. Commits are not allowed to be directly pushed to this branch.

## The Release Branch

The release branch is branched from the head of the `master` branch as soon as new work starts on the next release version. Features for the next release are developed in feature branches and rebased on top of this branch. This branch is named `release/next` and there can only be one of these branches at any given time.

Release branches are almost always pushed to the remote due to their longer-lived nature.

The reason temporary `release` branches are used, as opposed to a second eternal branch like `develop`, is so the current `release` branch can be easily updated when a `hotfix` or `docs` branch is integrated into the `master` branch, while keeping the history linear and clean.

### Creating the Release Branch

```
$ git checkout -b release/next master
```

### Updating the Release Branch

Keeping the release branch current is as simple as rebasing on `master` any time `master` changes.

```
$ git checkout release/next
$ git rebase master
$ git push --force-with-lease
```

### Finishing the Release Branch

```
$ git checkout release/next
$ git rebase -i master
$ git checkout master
$ git merge --ff-only release/next
$ git push origin master
$ git branch -d release/next
```

By using `rebase` instead of `merge --no-ff`, the linear history keeps the `master` branch clean and offers the opportunity to clean up the release history before integration.

If the release branch exists on remote, which it almost always should, remove it.

```
$ git push origin :release/next
```

At this time, the release must be versioned by using the version tool to bump the version file and tag.

```
$ version set version
```

## Feature Branches

New feature branches are branched from the head of the release branch and are short lived branches where the majority of work takes place. They are used to develop new features and bugfixes for an upcoming release. Feature branch names are prefixed with `feature/` and will look similar to `feature/the-feature`. Feature branches exist only in the developer's repository and usually only pushed if the feature will take a long time to develop, as a backup precaution.

Feature branches are used instead of direct commits to the `release` branch in order to allow multiple features to be developed simultaneously. If a feature's development becomes blocked, development in other areas can continue cleanly.

### Creating a Feature Branch

```
$ git checkout -b feature/the-feature release/version
```

### Updating a Feature Branch

Updating a feature branch is done with `rebase` similar to updating the release branch.

```
$ git checkout feature/the-feature
$ git rebase release/version
$ git push --force-with-lease
```

### Finishing a Feature Branch

```
$ git checkout feature/the-feature
$ git rebase -i release/version
$ git checkout release/version
$ git merge --ff-only feature/the-feature
$ git push origin release/version
$ git branch -d feature/the-feature
```

If the feature branch was pushed to remote, it will need to be removed.

```
$ git push origin :feature/the-feature
```

## Hotfix Branches

Hotfixes occur when a critical failure is exposed in a released version and fixing the bug in the next release would take too long for users to get the fix. Hotfix branches are extremely short-lived branches branched from the `master` branch. These branches are named `hotfix/fix`.

A hotfix will always increase the patch version.

### Creating a Hotfix Branch

```
$ git checkout -b hotfix/fix master
```

### Updating a Hotfix Branch

Hotfix branches are so short-lived, they shouldn't ever need to be updated. If a hotfix branch needs to be updated, it likely means that a `release` branch was integrated into the `master` branch, and it has the same bug the hotfix is currently fixing. This is just stupid.

If you must update a hotfix branch, rebase it on top of `master`.

```
$ git checkout hotfix/fix
$ git rebase master
```

### Finishing a Hotfix Branch

```
$ git checkout hotfix/fix
$ git rebase -i master
$ git checkout master
$ git merge --ff-only hotfix/fix
$ git push origin master
$ git branch -d hotfix/fix
```

At this time, the patch release must be versioned by using the version tool to bump the version file and tag.

```
$ version set version
```

The final step is to update the `release` branch by rebasing it on master. See: [Updating the Next Release Branch](#updating-the-release-branch).

## Documentation Branches

Sometimes the documentation in the `master` branch needs to be edited.

Documentation branches are treated the same way as hotfix branches, with two exceptions: they are named `docs/the-change` and they do not increment the `master` branch version.

## Final Branches

Final branches are feature-frozen branches, or branches for final versions that will not receive any new feature development, but will continue to receive hotfixes. These branches, named `final/version`, are branched from `master` before moving into development for a new Minecraft version. For example, the final branch for Minecraft 1.12.2 looks like `final/1.12.2`.

Hotfixing a final branch works the same way as hotfixing the `master` branch. If a hotfix to a `final` branch is applicable to the `master` branch, create a `hotfix` branch from the `master` branch and use `git cherry-pick` to apply the relevant commits to the `master->hotfix` branch, then finalize the new branch. Version tags created from a `hotfix` in a `final` branch should never make their way into the `master` branch.

Final branches are never rebased, finalized, or deleted.

### Creating a Final Branch

```
$ git checkout -b final/version
```

## Alpha Releases

Alpha releases are compiled and released from the current `release` branch.

First, tag the branch using the next release version number and the alpha suffix:

```
$ version set 1.0.0-alpha0
```

Then compile and release:

```
$ gradle clean build
```