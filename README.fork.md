# ntfy-relay (Fork Version)

This repository is a **personal fork** of the excellent [ntfy-relay project by Ubaldo Porcheddu](https://github.com/eja-dev/ntfy-relay).  
I highly appreciate the original work — it's simple, powerful, and just works. I've installed it on almost every Android device I use.

## ✨ Why This Fork?

While using ntfy-relay daily, I found a few situations where I wanted slightly different behavior.  
This fork introduces several small enhancements tailored to my personal usage.

## 🔧 Modifications and Additions

- 🔄 **Suppress repeated notifications within a short interval**  
  Avoids flooding when certain apps (e.g. during phone calls) emit the same notification rapidly.

- 🔕 **Exclude "silent" notifications introduced in Android 10+**  
  Notifications with low/minimum importance (e.g. background sync) are ignored.

- 🔁 **Ignore notifications from the official ntfy app** (`io.heckel.ntfy`)  
  Prevents endless notification loops when ntfy-relay forwards notifications that were originally received via ntfy itself.

## 🙏 Acknowledgements

Huge thanks to [Ubaldo Porcheddu](https://github.com/eja-dev) for creating the original ntfy-relay.  
This fork simply builds on top of that solid foundation.

## 👤 Author

This fork is maintained by [MATSUOKA Hiroshi](https://github.com/matsuoka).
