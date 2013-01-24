#!/bin/sh
# Push javadoc files to a website hosted by github <http://pages.github.com/>.
# Before executing this script, generate the javadoc files into build/docs/javadoc/.
git checkout gh-pages || exit $?

# Clear out the old files:
rm -rf docs/* 
# Replace them with new files and commit them:
cp -pr AdamBots-FIRST-2013-Robot-Code/docs/* javadoc \
&& git add javadoc \
&& git commit -a -m "generated javadoc"
ERROR=$?

git checkout master || exit $?
[ $ERROR -eq 0 ] || exit $ERROR
git push github master || exit $?
git push github gh-pages || exit $?