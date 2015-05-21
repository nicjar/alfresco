#!/usr/bin/perl
use File::Copy;

$java_path = `which java`;
chomp($java_path);
if ($java_path =~ /not found/) {
  print "Can't find a java executable. Please specify the path to a java executable on your system\n";
  $java_path = <STDIN>;
  chomp($java_path);
  if (!$java_path) { 
    print "No java executable defined. Exiting\n";
    exit;
  }
}

# my $install_base = "/usr/local";
my $install_base = "$ENV{HOME}";
print "Where do you want Alfresco installed? [$install_base] ";
$answer = <STDIN>;
chomp($answer);
if ($answer) { 
  $install_base = $answer;
}
my $jar_dir = "$install_base/alfresco";
my $bindir = "$install_base/bin";

print "\nAlfresco files will be installed in $jar_dir\n";
print "The alfresco start-up script will be installed in $bindir\n";
print "Is this ok? [Y/n]";
$answer = <STDIN>;
if ($answer =~ /^[nN]/) {
  print "Alfresco will not be installed. Exiting\n";
  exit;
}

if (! -d $bindir) {
  print "$bindir doesn't exist. Is it ok to create $bindir? [Y/n] ";
  $answer = <STDIN>;
  if ($answer =~ /^[nN]/) {
    print "Can't create $bindir. Exiting\n";
    exit;
  }
  mkdir $bindir, 0755 or die "Can't create $bindir";
}

if (! -d $jar_dir) {
  print "$jar_dir doesn't exist. Is it ok to create $jar_dir? [Y/n] ";
  $answer = <STDIN>;
  if ($answer =~ /^[nN]/) {
    print "Can't create $jar_dir. Exiting\n";
    exit;
  }
  mkdir $jar_dir, 0755 or die "Can't create $jar_dir.";
}




open ALFSCRIPT, ">$bindir/alfresco" or die "Can't create $bindir/alfresco";
print ALFSCRIPT "#!/bin/csh\n";
print ALFSCRIPT "setenv CLASSPATH $jar_dir/alfresco.jar\n";
print ALFSCRIPT "$java_path -ms8m -mx48m -ss1m -oss2m alfresco.Alfresco \$*\n";
close ALFSCRIPT;


copy("alfresco.jar", "$jar_dir/alfresco.jar");

