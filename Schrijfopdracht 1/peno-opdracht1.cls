%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%                                                    %%%
%%%   Class bestand voor P&O: Computerwetenschappen.   %%%
%%%                                                    %%%
%%%   Schrijfopdracht 1                                %%%
%%%                                                    %%%
%%%   7 oktober 2013                                   %%%
%%%   Versie 1.1                                       %%%
%%%                                                    %%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
\NeedsTeXFormat{LaTeX2e}[1995/12/01]
\ProvidesClass{peno}[2012/09/25 v1.0 P&O verslag document class]
\def\thepagelimit{2}
\ProcessOptions
\LoadClass[10pt,a4paper]{article}
\RequirePackage[dutch]{babel}
\RequirePackage{color}
\RequirePackage{everypage}
\RequirePackage{geometry}
\RequirePackage{graphicx}
\geometry{margin=3cm}
\def\team#1{\def\theteam{#1}}
\author{\bf Team \theteam}
\title{P\&O Computerwetenschappen: Schrijfopdracht 1}
\date{}
\def\pagelimittext{PAGE LIMIT}
\def\pagelimitfontsize{2cm}
\def\pagelimitscale{5}
\newcommand\sc@wm@inbox[1]{%
  \setbox\@tempboxa\vbox to \z@{%
    \vskip -1in \moveleft 1in \vbox{%
      \hbox to \z@{%
        #1\hss}%
    }%
    \vss%
  }
  \dp\@tempboxa\z@
  \box\@tempboxa%
}
\newcommand\sc@wm@printtext[1]{%
  \setlength{\@tempdimb}{.5\paperwidth}%
  \setlength{\@tempdimc}{-.5\paperheight}%
  \setlength{\unitlength}{1pt}%
  \put(\strip@pt\@tempdimb,\strip@pt\@tempdimc){%
    \makebox(0,0){%
      \rotatebox{45}{\scalebox{\pagelimitscale}{%
        \textcolor[gray]{0.7}%
          {\fontsize{\pagelimitfontsize}{1.2\pagelimitfontsize}\selectfont{\pagelimittext}}%
      }}%
    }%
  }%
}
\AddEverypageHook{%
  \ifnum\thepage>\thepagelimit%
    \sc@wm@inbox{\sc@wm@printtext}
  \fi
}
\endinput
